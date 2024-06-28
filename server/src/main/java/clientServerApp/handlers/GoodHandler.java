package clientServerApp.handlers;

import clientServerApp.entity.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static clientServerApp.Main.dbc;
import static clientServerApp.Main.objectMapper;
import static clientServerApp.handlers.HandlerTools.*;

public class GoodHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Optional<String> token = Optional.ofNullable(exchange.getRequestHeaders().getFirst("Authorization"));
        if (token.isPresent() && verifyToken(token.get())) {
            String login = getLoginFromToken(token.get());
            exchange.setAttribute("login", login);
            switch (exchange.getRequestMethod()) {
                case "OPTIONS":
                    handleOptionsRequest(exchange);
                    break;
                case "GET":
                    handleGetRequest(exchange);
                    break;
                case "PUT":
                    handlePutRequest(exchange);
                    break;
                case "POST":
                    handlePostRequest(exchange);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } else {
            sendResponse(exchange, 403, "Forbidden");
        }
    }

    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String id = getIdFromPath(exchange.getRequestURI().getPath());

        if (id.equals("good")) {
            handleGetAllGoodsRequest(exchange);
        } else if (id.equals("price")) {
            handleGetPriceRequest(exchange);
        } else if (id.equals("search")) {
            handleSearchRequest(exchange);
        } else {
            handleGetGoodByIdRequest(exchange, id);
        }
    }

    private void handleGetAllGoodsRequest(HttpExchange exchange) throws IOException {
        String response = getGoodsInfo();
        sendResponse(exchange, 200, response);
    }

    private void handleGetPriceRequest(HttpExchange exchange) throws IOException {
        String response = getPrice();
        sendResponse(exchange, 200, response);
    }

    private void handleSearchRequest(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = queryToMap(query);
        String name = params.get("name");
        String response = getProductByName(name);
        if (response.equals("error"))
            sendResponse(exchange, 404, "Product not found");
        else
            sendResponse(exchange, 200, response);
    }

    private void handleGetGoodByIdRequest(HttpExchange exchange, String id) throws IOException {
        if (isGoodExists(id)) {
            String response = getGoodInfo(id);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    private void handlePutRequest(HttpExchange exchange) throws IOException {
        String reqBody = requestBodyToString(exchange);
        Product product = jsonToProduct(reqBody);

        if (isValidGood(product)) {
            String id = createGood(product);
            if (id.equals("-1"))
                sendResponse(exchange, 409, "Product already exists");
            else {
                String response = "Created with id: " + id;
                sendResponse(exchange, 201, response);
            }
        } else {
            sendResponse(exchange, 409, "Product is nor valid");
        }
    }

    private Product jsonToProduct(String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);

            String name = rootNode.get("name").asText();
            double price = rootNode.get("price").asDouble();
            int amount = rootNode.get("amount").asInt();
            String description = rootNode.get("description").asText();
            String producer = rootNode.get("producer").asText();
            int groupId = rootNode.get("group_id").asInt();

            return new Product(name, price, amount, description, producer, groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String id = getIdFromPath(exchange.getRequestURI().getPath());

        if (id.equals("add")) {
            String path = exchange.getRequestURI().getPath();
            String newId = getPrevId(path);
            if (isGoodExists(newId)) {
                String reqBody = requestBodyToString(exchange);
                addGood(Integer.valueOf(newId), Integer.valueOf(reqBody));
                sendResponse(exchange, 200, "Product added");
            } else {
                sendResponse(exchange, 404, "Product Not Found");
            }
        } else if (id.equals("subtract")) {
            String path = exchange.getRequestURI().getPath();
            String newId = getPrevId(path);
            if (isGoodExists(newId)) {
                String reqBody = requestBodyToString(exchange);
                int status = subtractGood(Integer.valueOf(newId), Integer.valueOf(reqBody));
                if (status == 1)
                    sendResponse(exchange, 200, "Product subtracted");
                else
                    sendResponse(exchange, 409, "Couldn't subtract product");
            } else {
                sendResponse(exchange, 404, "Product Not Found");
            }
        } else {
            if (isGoodExists(id)) {
                String reqBody = requestBodyToString(exchange);
                Product product = jsonToProduct(reqBody);

                if (isValidGood(product)) {
                    int status = updateGood(id, product);
                    if (status == 1)
                        sendResponse(exchange, 204, "Product updated");
                    else
                        sendResponse(exchange, 409, "Product with this name already exists");
                } else {
                    sendResponse(exchange, 409, "Product is not valid");
                }
            } else {
                sendResponse(exchange, 404, "Product Not Found");
            }
        }
    }

    private int subtractGood(Integer id, int amount) {
        Product p = dbc.getProduct(id);
        if (p.getAmount() - amount < 0) return -1;
        p.setAmount(p.getAmount() - amount);
        return dbc.updateProduct(dbc.getProductName(id), p);
    }

    private void addGood(Integer id, int amount) {
        Product p = dbc.getProduct(id);
        p.setAmount(p.getAmount() + amount);
        dbc.updateProduct(dbc.getProductName(id), p);
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String id = getIdFromPath(exchange.getRequestURI().getPath());

        if (isGoodExists(id)) {
            deleteGood(id);
            sendResponse(exchange, 204, "No Content");
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    private String getPrice() {
        return dbc.getPrice();
    }

    private boolean isGoodExists(String id) {
        return dbc.productExists(dbc.getProductName(Integer.parseInt(id)));
    }

    private String getGoodInfo(String id) {
        try {
            return objectMapper.writeValueAsString(dbc.getProduct(Integer.valueOf(id)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getProductByName(String name) {
        try {
            Product p = dbc.getProduct(name);
            if (p == null) return "error";
            return objectMapper.writeValueAsString(p);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getGoodsInfo() {
        return dbc.showProducts();
    }

    private boolean isValidGood(Product p) {
        return p.getName() != null &&
                p.getAmount() >= 0 &&
                p.getDescription() != null &&
                p.getPrice() > 0 &&
                p.getProducer() != null &&
                dbc.groupExists(p.getGroup_id());
    }

    private String createGood(Product p) {
        int status = dbc.createProduct(p);
        if (status == 1)
            return String.valueOf(dbc.getProductId(p.getName()));
        else
            return "-1";
    }

    private int updateGood(String id, Product p) {
        return dbc.updateProduct(dbc.getProductName(Integer.parseInt(id)), p);
    }

    private void deleteGood(String id) {
        dbc.deleteProduct(dbc.getProductName(Integer.parseInt(id)));
    }
}