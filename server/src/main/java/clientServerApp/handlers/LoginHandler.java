package clientServerApp.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static clientServerApp.Main.USERS;
import static clientServerApp.handlers.HandlerTools.*;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Optional<String> requestMethod = Optional.ofNullable(exchange.getRequestMethod());
        if (requestMethod.isPresent()) {
            switch (requestMethod.get()) {
                case "POST":
                    handlePostRequest(exchange);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = queryToMap(query);

        String login = params.get("login");
        String password = params.get("password");
        String passwordMD5 = md5(password);

        if (USERS.containsKey(login) && USERS.get(login).equals(password)) {
            String token = generateToken(login);
            System.out.println("Token for user " + login + ": " + token);
            sendResponse(exchange, 200, token);
        } else {
            System.out.println(USERS);
            sendResponse(exchange, 401, "Unauthorized access");
        }
    }
}