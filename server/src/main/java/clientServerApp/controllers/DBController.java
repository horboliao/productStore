package clientServerApp.controllers;

import clientServerApp.entity.Group;
import clientServerApp.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBController {
    private final Connection connection;
    private GroupController groupController;
    private ProductController productController;
    private static final String URL = "jdbc:postgresql://localhost:5432/goods";
    private static final String USERNAME = "test_user";
    private static final String PASSWORD = "test_password33";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    public DBController(String fileName) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        groupController = new GroupController(connection);
        productController = new ProductController(connection);

        initGroupTable();
        initProductTable();
    }

    public void initProductTable() {
        try {
            PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS products " +
                    "(id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "price DECIMAL NOT NULL," +
                    "amount INTEGER NOT NULL, " +
                    "description VARCHAR(300) NOT NULL, " +
                    "producer VARCHAR(200) NOT NULL, " +
                    "group_id INTEGER NOT NULL, FOREIGN KEY(group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE CASCADE);");
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't create products table");
            e.printStackTrace();
        }
    }

    public void initGroupTable() {
        try {
            PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS groups " +
                    "(id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "description VARCHAR(300) NOT NULL);");
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't create groups table");
            e.printStackTrace();
        }
    }

    public static Integer getGroupId(String name){
        return GroupController.getGroupId(name);
    }

    public int createProduct(Product p){
        return productController.createProduct(p);
    }

    public int createGroup(Group g){
        return groupController.createGroup(g);
    }

    public String showGroups(){
        return groupController.showGroups();
    }

    public String showProducts(){
        return productController.showProducts();
    }

    public int updateProduct(String name, Product product_updated){
        Integer id = productController.getProductId(name);
        if(id != null)
            return productController.updateProduct(product_updated, id);
        return -1;
    }

    public int updateGroup(String name, Group group_updated){
        Integer id = GroupController.getGroupId(name);
        if(id != null)
            return groupController.updateGroup(group_updated, id);
        return -1;
    }

    public void deleteGroups(){
        try(Statement statement = connection.createStatement()){
            String query = "DELETE FROM groups";
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("Couldn't delete groups.");
        }
    }

    public void deleteProducts(){
        try(Statement statement = connection.createStatement()){
            String query = "DELETE FROM products";
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("Couldn't delete products.");
        }
    }

    public void dropTables(){
        try(Statement statement = connection.createStatement()){
            String query = "drop table products";
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("Couldn't drop tables.");
        }

        try(Statement statement = connection.createStatement()){
            String query = "drop table groups";
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("Couldn't drop tables.");
        }
    }

    public void deleteGroup(String name) {
        groupController.deleteGroup(name);
    }

    public void deleteProduct(String name) {
        productController.deleteProduct(name);
    }

    public int showProductsFiltered(List<String> names, int lowestPrice, int highestPrice, List<String> producers, List<String> groupNames){
        List<Integer> groupIds = new ArrayList<Integer>();

        if(groupNames != null)
            for (String name : groupNames) {
                Integer id = GroupController.getGroupId(name);
                if(id != null) groupIds.add(id);
            }
        return productController.showProductsFiltered(names, lowestPrice, highestPrice, producers, groupIds);
    }

    public boolean productExists(String name){
        return productController.exists(name);
    }

    public boolean groupExists(String name){
        return groupController.exists(name);
    }

    public boolean groupExists(Integer id){
        return groupController.exists(id);
    }

    public Integer getProductId(String name){
        return productController.getProductId(name);
    }

    public String getProductName(int id){
        return productController.getProductName(id);
    }

    public Product getProduct(Integer id) {
        return productController.getProduct(id);
    }
    public Product getProduct(String name) {
        if(!productExists(name)) return null;
        return productController.getProduct(name);
    }

    public Group getGroup(Integer id) {
        return groupController.getGroup(id);
    }

    public String getGroupName(int id) {
        return groupController.getGroupName(id);
    }

    public String showProductsInGroup(Integer id) {
        return productController.showProductsInGroup(id);
    }

    public String getPrice() {
        return String.valueOf(productController.getPrice());
    }


    public String getProductsPrice(Integer id) {
        return String.valueOf(productController.getPriceInGroup(id));
    }
}
