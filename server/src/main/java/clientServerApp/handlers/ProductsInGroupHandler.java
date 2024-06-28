package clientServerApp.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;

import static clientServerApp.Main.dbc;
import static clientServerApp.handlers.HandlerTools.*;

public class ProductsInGroupHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Optional<String> token = Optional.ofNullable(exchange.getRequestHeaders().getFirst("Authorization"));
        if (token.isPresent() && verifyToken(token.get())) {
            String login = getLoginFromToken(token.get());
            exchange.setAttribute("login", login);
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGetRequest(exchange);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } else {
            sendResponse(exchange, 403, "Forbidden");
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String id = getIdFromPath(exchange.getRequestURI().getPath());
        if (id.equals("price")) {
            handleGetPriceRequest(exchange, id);
        } else {
            handleGetGroupProductsRequest(exchange, id);
        }
    }

    private void handleGetPriceRequest(HttpExchange exchange, String id) throws IOException {
        String newId = getPrevId(exchange.getRequestURI().getPath());
        if (isGroupExists(newId)) {
            String response = getGoodsPrice(Integer.valueOf(newId));
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 409, "Group doesn't exist");
        }
    }

    private void handleGetGroupProductsRequest(HttpExchange exchange, String id) throws IOException {
        if (isGroupExists(id)) {
            String response = getGoodsInfo(Integer.valueOf(id));
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 409, "Group doesn't exist");
        }
    }

    private String getGoodsPrice(Integer id) {
        return dbc.getProductsPrice(id);
    }

    private String getGoodsInfo(Integer id) {
        return dbc.showProductsInGroup(id);
    }
}