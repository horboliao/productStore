package clientServerApp;

import clientServerApp.controllers.DBController;
import clientServerApp.entity.Group;
import clientServerApp.entity.Product;
import clientServerApp.handlers.GoodHandler;
import clientServerApp.handlers.GroupHandler;
import clientServerApp.handlers.LoginHandler;
import clientServerApp.handlers.ProductsInGroupHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final Map<String, String> USERS = new HashMap<>();
    public static DBController dbc;
    public static ObjectMapper objectMapper;

    public static void main(String[] args) throws IOException {
        USERS.put("user1", "5f4dcc3b5aa765d61d8327deb882cf99"); // "password"
        USERS.put("user2", "098f6bcd4621d373cade4e832627b4f6"); // "test"

        dbc = new DBController("goods"); // replace with your PostgreSQL database name
        objectMapper = new ObjectMapper();

        // Create groups
        Group g1 = new Group("dairy products", "dairy products description");
        Group g2 = new Group("beverages", "beverages description");
        dbc.createGroup(g1);
        dbc.createGroup(g2);

        // Create products
        Product p1 = new Product("milk", 1.25, 5, "milk description", "milk producer", dbc.getGroupId("dairy products"));
        Product p2 = new Product("cheese", 2.5, 7, "cheese description", "milk producer", dbc.getGroupId("dairy products"));
        Product p3 = new Product("orange juice", 1.75, 5, "orange juice description", "juice producer", dbc.getGroupId("beverages"));
        dbc.createProduct(p1);
        dbc.createProduct(p2);
        dbc.createProduct(p3);

        // Update a product
        p1.setPrice(1.50);
        dbc.updateProduct(p1.getName(), p1);

        // Delete a product
        dbc.deleteProduct(p2.getName());

        // Show all groups
        System.out.println(dbc.showGroups());

        // Show all products
        System.out.println(dbc.showProducts());

        // Show products in a group
        System.out.println(dbc.showProductsInGroup(dbc.getGroupId("dairy products")));

        // Show total price of all products
        System.out.println(dbc.getPrice());

        // Show total price of products in a group
        System.out.println(dbc.getProductsPrice(dbc.getGroupId("dairy products")));


// Create a new HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Add handlers to the server
        server.createContext("/login", new LoginHandler());
        server.createContext("/api/good", new GoodHandler());
        server.createContext("/api/group", new GroupHandler());
        server.createContext("/api/goods-in-group", new ProductsInGroupHandler());

        // Start the server
        server.start();

        System.out.println("Server started on port 8000");
    }
}


