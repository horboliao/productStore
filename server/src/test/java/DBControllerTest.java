import clientServerApp.controllers.DBController;
import clientServerApp.controllers.GroupController;
import clientServerApp.controllers.ProductController;
import clientServerApp.entity.Group;
import clientServerApp.entity.Product;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DBControllerTest {

    private DBController dbController;
    private GroupController groupController;
    private ProductController productController;
    private static Connection connection;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DBController.getConnection();
    }

    @BeforeEach
    public void setup() {
        dbController = new DBController("test_db");
        dbController.dropTables();
        dbController.initGroupTable();
        dbController.initProductTable();
    }

    @AfterEach
    public void cleanup() {
        dbController.dropTables();
    }

    @Test
    public void testInitTables() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet groupsTable = metaData.getTables(null, null, "groups", null);
        assertTrue(groupsTable.next(), "Groups table should be created");

        ResultSet productsTable = metaData.getTables(null, null, "products", null);
        assertTrue(productsTable.next(), "Products table should be created");
    }

    @Test
    public void testCreateGroup() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);
        assertTrue(groupId > 0, "Group should be created and have a valid ID");
    }

    @Test
    public void testCreateProduct() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        Product product = new Product("Smartphone", 699.99, 50, "Latest model", "TechCorp", groupId);
        int productId = productController.createProduct(product);
        assertTrue(productId > 0, "Product should be created and have a valid ID");
    }

    @Test
    public void testShowGroups() {
        Group group1 = new Group("Electronics", "Electronic devices and gadgets");
        Group group2 = new Group("Books", "Books and literature");
        groupController.createGroup(group1);
        groupController.createGroup(group2);

        String groups = groupController.showGroups();
        assertTrue(groups.contains("Electronics"), "Groups list should include 'Electronics'");
        assertTrue(groups.contains("Books"), "Groups list should include 'Books'");
    }

    @Test
    public void testShowProducts() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        Product product1 = new Product("Smartphone", 699.99, 50, "Latest model", "TechCorp", groupId);
        Product product2 = new Product("Laptop", 999.99, 30, "High-performance laptop", "TechCorp", groupId);
        productController.createProduct(product1);
        productController.createProduct(product2);

        String products = productController.showProducts();
        assertTrue(products.contains("Smartphone"), "Products list should include 'Smartphone'");
        assertTrue(products.contains("Laptop"), "Products list should include 'Laptop'");
    }


    @Test
    public void testUpdateProduct() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        Product product = new Product("Smartphone", 699.99, 50, "Latest model", "TechCorp", groupId);
        int productId = productController.createProduct(product);

        Product updatedProduct = new Product("Smartphone", 749.99, 60, "Updated model", "TechCorp", groupId);
        int updateResult = productController.updateProduct(updatedProduct, productId);

        assertEquals(1, updateResult, "Product should be updated successfully");
        assertEquals(749.99, productController.getProduct(productId).getPrice(), "Product price should be updated");
    }

    @Test
    public void testUpdateGroup() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        Group updatedGroup = new Group("Electronics", "Updated description");
        int updateResult = groupController.updateGroup(updatedGroup, groupId);

        assertEquals(1, updateResult, "Group should be updated successfully");
        assertEquals("Updated description", groupController.getGroup(groupId).getDescription(), "Group description should be updated");
    }

    @Test
    public void testDeleteGroup() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        groupController.createGroup(group);

        groupController.deleteGroup("Electronics");
        assertFalse(groupController.exists("Electronics"), "Group should be deleted");
    }

    @Test
    public void testDeleteProduct() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        Product product = new Product("Smartphone", 699.99, 50, "Latest model", "TechCorp", groupId);
        productController.createProduct(product);

        productController.deleteProduct("Smartphone");
        assertFalse(productController.exists("Smartphone"), "Product should be deleted");
    }

    @Test
    public void testShowProductsFiltered() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        Product product1 = new Product("Smartphone", 699.99, 50, "Latest model", "TechCorp", groupId);
        Product product2 = new Product("Laptop", 999.99, 30, "High-performance laptop", "TechCorp", groupId);
        productController.createProduct(product1);
        productController.createProduct(product2);

        List<String> names = Arrays.asList("Smartphone");
        int filteredProducts = productController.showProductsFiltered(names, 500, 800, Arrays.asList("TechCorp"), Arrays.asList(groupId));

        assertEquals(1, filteredProducts, "Should return 1 filtered product");
    }


    @Test
    public void testGetGroupByName() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        String groupName = groupController.getGroupName(groupId);
        assertEquals("Electronics", groupName, "Group name should be 'Electronics'");
    }

    @Test
    public void testGetProductByName() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        Product product = new Product("Smartphone", 699.99, 50, "Latest model", "TechCorp", groupId);
        int productId = productController.createProduct(product);

        Product fetchedProduct = productController.getProduct(productId);
        assertEquals("Smartphone", fetchedProduct.getName(), "Product name should be 'Smartphone'");
    }

    @Test
    public void testGetPriceInGroup() {
        Group group = new Group("Electronics", "Electronic devices and gadgets");
        int groupId = groupController.createGroup(group);

        Product product1 = new Product("Smartphone", 699.99, 50, "Latest model", "TechCorp", groupId);
        Product product2 = new Product("Laptop", 999.99, 30, "High-performance laptop", "TechCorp", groupId);
        productController.createProduct(product1);
        productController.createProduct(product2);

        double totalPrice = productController.getPriceInGroup(groupId);
        assertEquals(699.99 * 50 + 999.99 * 30, totalPrice, 0.01, "Total price of products in group should be calculated correctly");
    }

    @AfterAll
    public static void closeDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
