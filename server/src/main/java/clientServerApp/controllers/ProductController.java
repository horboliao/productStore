package clientServerApp.controllers;

import clientServerApp.entity.Product;

import java.sql.*;
import java.util.List;

import static clientServerApp.Main.dbc;

public class ProductController {
    private Connection connection;

    public ProductController(Connection connection) {
        this.connection = connection;
    }

    public int createProduct(Product product) {
        if (exists(product.getName())) return -1;
        try {
            PreparedStatement st = connection.prepareStatement(
                    "INSERT INTO products (name, price, amount, description, producer, group_id) " +
                            "values (?, ?, ?, ?, ?, ?)");

            st.setString(1, product.getName());
            st.setDouble(2, product.getPrice());
            st.setInt(3, product.getAmount());
            st.setString(4, product.getDescription());
            st.setString(5, product.getProducer());
            st.setInt(6, product.getGroup_id());
            st.execute();
            return 1;
        } catch (SQLException e) {
            System.out.println("Couldn't add product " + product.getName());
            e.printStackTrace();
            return -1;
        }
    }

    public int updateProduct(Product product, Integer id) {
        if (exists(product.getName()) && !product.getName().equals(getProductName(id))) return -1;
        try (PreparedStatement st = connection.prepareStatement("UPDATE products SET name = ?, price = ?, amount = ?, description = ?, producer = ?, group_id = ?  WHERE id = ?")) {
            st.setString(1, product.getName());
            st.setDouble(2, product.getPrice());
            st.setDouble(3, product.getAmount());
            st.setString(4, product.getDescription());
            st.setString(5, product.getProducer());
            st.setInt(6, product.getGroup_id());
            st.setInt(7, id);
            st.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println("Couldn't update product " + product.getName());
        }
        return -1;
    }

    public void deleteProduct(String name) {
        if (!exists(name)) return;
        try (PreparedStatement st = connection.prepareStatement("DELETE FROM products WHERE name = ?")) {
            st.setString(1, name);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't delete product " + name);
        }
    }

    public String showProducts() {
        try {
            String products = "[";
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM products");
            while (res.next()) {
                String json = "{\"id\":\"" + res.getInt("id") +
                        "\", \"name\":\"" + res.getString("name") +
                        "\", \"price\":" + res.getDouble("price") +
                        ", \"amount\":" + res.getInt("amount") +
                        ", \"description\":\"" + res.getString("description") +
                        "\", \"producer\":\"" + res.getString("producer") +
                        "\", \"group_id\":" + res.getInt("group_id") +
                        ", \"group_name\":\"" + dbc.getGroupName(res.getInt("group_id")) +
                        "\", \"sum_price\":" + (res.getDouble("price") * res.getInt("amount")) + "}";
                products += json + ", ";
            }
            if (products.length() > 1)
                products = products.substring(0, products.length() - 2);
            products += "]";
            res.close();
            st.close();
            return products;
        } catch (SQLException e) {
            System.out.println("Couldn't show products.");
            e.printStackTrace();
        }
        return null;
    }

    public int showProductsFiltered(List<String> names, int lowestPrice, int highestPrice, List<String> producers, List<Integer> groupIds) {
        try {
            Statement st = connection.createStatement();
            String query = "SELECT * FROM products";

            boolean and = false;

            if (names != null && names.size() > 0) {
                and = true;
                query += " WHERE name IN (";
                for (int i = 0; i < names.size() - 1; i++)
                    query += "'" + names.get(i) + "', ";
                query += "'" + names.get(names.size() - 1) + "')";
            }

            if (and) query += " AND price BETWEEN ";
            else query += " WHERE price BETWEEN ";
            query += lowestPrice + " AND " + highestPrice;

            if (producers != null && producers.size() > 0) {
                query += " AND producer IN (";
                for (int i = 0; i < producers.size() - 1; i++)
                    query += "'" + producers.get(i) + "', ";
                query += "'" + producers.get(producers.size() - 1) + "')";
            }

            if (groupIds != null && groupIds.size() > 0) {
                query += " AND group_id IN (";
                for (int i = 0; i < groupIds.size() - 1; i++)
                    query += groupIds.get(i) + ", ";
                query += groupIds.get(groupIds.size() - 1) + ")";
            }

            System.out.println(query);

            int cnt = 0;

            ResultSet res = st.executeQuery(query);
            while (res.next()) {
                cnt++;
                String s = "";
                s += "id: " + res.getInt("id");
                s += ", name: " + res.getString("name");
                s += ", price: " + res.getDouble("price");
                s += ", producer: " + res.getString("producer");
                System.out.println(s);
            }

            res.close();
            st.close();

            return cnt;
        } catch (SQLException e) {
            System.out.println("Couldn't show filtered products.");
            e.printStackTrace();
            return -1;
        }
    }

    public boolean exists(String name) {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM products WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Couldn't check if product exists.");
            return false;
        }
    }

    public Integer getProductId(String name){
        try(Statement statement = connection.createStatement()){
            String sql = String.format("SELECT * FROM products WHERE name = '%s'", name);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                return id;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Can't get product id.");
            e.printStackTrace();
            return null;
        }
    }

    public String getProductName(Integer id) {
        try (Statement statement = connection.createStatement()){
            String sql = "SELECT * FROM products WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                return name;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Can't get product name.");
            e.printStackTrace();
            return null;
        }
    }

    public Product getProduct(Integer id) {
        try (Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM products WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Product product = new Product(
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("amount"),
                    resultSet.getString("description"),
                    resultSet.getString("producer"),
                    resultSet.getInt("group_id"));
            return product;
        } catch (SQLException e) {
            System.out.println("Couldn't get product.");
            return null;
        }
    }

    public Product getProduct(String name) {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM products WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            Product product = new Product(
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("amount"),
                    resultSet.getString("description"),
                    resultSet.getString("producer"),
                    resultSet.getInt("group_id"));
            return product;
        } catch (SQLException e) {
            System.out.println("Couldn't get product.");
            return null;
        }
    }

    public String showProductsInGroup(Integer id) {
        try {
            String products = "[";
            Statement st = connection.createStatement();
            String query = "SELECT * FROM products WHERE group_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                String json = "{\"id\":\"" + res.getInt("id") +
                        "\", \"name\":\"" + res.getString("name") +
                        "\", \"price\":" + res.getDouble("price") +
                        ", \"amount\":" + res.getInt("amount") +
                        ", \"description\":\"" + res.getString("description") +
                        "\", \"producer\":\"" + res.getString("producer") +
                        "\", \"group_id\":" + res.getInt("group_id") +
                        ", \"group_name\":\"" + dbc.getGroupName(res.getInt("group_id")) +
                        "\", \"sum_price\":" + (res.getDouble("price") * res.getInt("amount")) + "}";

                products += json + ", ";
            }
            if (products.length() > 1)
                products = products.substring(0, products.length() - 2);
            products += "]";
            res.close();
            st.close();
            return products;
        } catch (SQLException e) {
            System.out.println("Couldn't show products.");
            e.printStackTrace();
        }
        return null;
    }

    public double getPrice() {
        try {
            double sum = 0;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM products");
            while (res.next())
                sum += res.getDouble("price") * res.getInt("amount");
            res.close();
            st.close();
            return sum;
        } catch (SQLException e) {
            System.out.println("Couldn't get price.");
            e.printStackTrace();
        }
        return -1;
    }

    public double getPriceInGroup(Integer id) {
        try {
            double sum = 0;
            Statement st = connection.createStatement();
            String query = "SELECT * FROM products WHERE group_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
                sum += res.getDouble("price") * res.getInt("amount");
            res.close();
            st.close();
            return sum;
        } catch (SQLException e) {
            System.out.println("Couldn't get price.");
            e.printStackTrace();
        }
        return -1;
    }
}