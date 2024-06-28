package clientServerApp.handlers;

import clientServerApp.entity.Group;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static clientServerApp.Main.dbc;
import static clientServerApp.Main.objectMapper;
import static clientServerApp.handlers.HandlerTools.*;

public class GroupHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String token = exchange.getRequestHeaders().getFirst("Authorization");
        if (token != null && verifyToken(token)) {
            String login = getLoginFromToken(token);
            exchange.setAttribute("login", login);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                handleOptionsRequest(exchange);
            } else {
                String method = exchange.getRequestMethod();

                if ("GET".equals(method)) {
                    handleGetRequest(exchange);
                } else if ("PUT".equals(method)) {
                    handlePutRequest(exchange);
                } else if ("POST".equals(method)) {
                    handlePostRequest(exchange);
                } else if ("DELETE".equals(method)) {
                    handleDeleteRequest(exchange);
                } else
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } else
            sendResponse(exchange, 403, "Forbidden");
    }
    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    // GET /api/group/{id}
    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String id = getIdFromPath(exchange.getRequestURI().getPath());

        if(id.equals("group")){
            String response = getGroupsInfo();
            sendResponse(exchange, 200, response);
        } else {
            if (isGroupExists(id)) {
                String response = getGroupInfo(id);
                sendResponse(exchange, 200, response);
            } else {
                sendResponse(exchange, 404, "Not Found");
            }
        }
    }

    // PUT /api/good
    private void handlePutRequest(HttpExchange exchange) throws IOException {
        String reqBody = requestBodyToString(exchange);
        Group group = jsonToGroup(reqBody);

        if (isValidGroup(group)) {
            String id = createGroup(group);
            if(id.equals("-1"))
                sendResponse(exchange, 409, "Group with this name already exists");
            else {
                String response = "Created with id: " + id;
                sendResponse(exchange, 201, response);
            }
        } else {
            sendResponse(exchange, 409, "Group is not valid");
        }
    }

    // POST /api/group/{id}
    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String id = getIdFromPath(exchange.getRequestURI().getPath());

        if (isGroupExists(id)) {
            String reqBody = requestBodyToString(exchange);
            Group group = jsonToGroup(reqBody);

            if (isValidGroup(group)) {
                int status = updateGroup(id, group);
                if(status == 1)
                    sendResponse(exchange, 204, "Group updated");
                else
                    sendResponse(exchange, 409, "Group with this name already exists");
            } else {
                sendResponse(exchange, 409, "Group is not valid");
            }
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    // DELETE /api/group/{id}
    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String id = getIdFromPath(exchange.getRequestURI().getPath());

        if (isGroupExists(id)) {
            deleteGroup(id);
            sendResponse(exchange, 204, "No Content");
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    private Group jsonToGroup(String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);

            String name = rootNode.get("name").asText();
            String description = rootNode.get("description").asText();

            return new Group(name, description);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isValidGroup(Group g) {
        return g.getName() != null &&
                g.getDescription() != null;
    }

    private String createGroup(Group g) {
        int status = dbc.createGroup(g);
        if(status == -1) return "-1";
        return String.valueOf(dbc.getGroupId(g.getName()));
    }

    private int updateGroup(String id, Group g) {
        return dbc.updateGroup(dbc.getGroupName(Integer.parseInt(id)), g);
    }

    private void deleteGroup(String id) {
        dbc.deleteGroup(dbc.getGroupName(Integer.parseInt(id)));
    }


    private String getGroupInfo(String id) {
        try {
            return objectMapper.writeValueAsString(dbc.getGroup(Integer.valueOf(id)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getGroupsInfo() {
        return dbc.showGroups();
    }
}
