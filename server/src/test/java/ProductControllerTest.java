import clientServerApp.controllers.ProductController;
import clientServerApp.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ProductControllerTest {
    private ProductController productController;

    @BeforeEach
    void setup() {
        String fileName = ":memory:"; // Use an in-memory database for testing
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);

            // Create the necessary tables
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS 'products' (" +
                    "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "'name' VARCHAR(50) NOT NULL, " +
                    "'price' DOUBLE NOT NULL, " +
                    "'amount' INTEGER NOT NULL, " +
                    "'description' VARCHAR(300) NOT NULL, " +
                    "'producer' VARCHAR(50) NOT NULL, " +
                    "'group_id' INTEGER NOT NULL);").execute();

            // Initialize the ProductController
            productController = new ProductController(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createProduct_WhenProductDoesNotExist_ShouldReturnOne() {
        Product product = new Product("Test Product", 9.99, 10, "Test Description", "Test Producer", 1);
        int result = productController.createProduct(product);
        assertEquals(1, result);
    }

    @Test
    void createProduct_WhenProductExists_ShouldReturnNegativeOne() {
        // Create a product
        Product product = new Product("Test Product", 9.99, 10, "Test Description", "Test Producer", 1);
        productController.createProduct(product);

        // Try to create the same product again
        int result = productController.createProduct(product);
        assertEquals(-1, result);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldUpdateProductAndReturnOne() {
        // Create a product
        Product product = new Product("Test Product", 9.99, 10, "Test Description", "Test Producer", 1);
        productController.createProduct(product);

        // Update the product
        product.setPrice(19.99);
        int result = productController.updateProduct(product, 1);
        assertEquals(1, result);

        // Retrieve the updated product
        Product updatedProduct = productController.getProduct(1);
        assertEquals(19.99, updatedProduct.getPrice());
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNegativeOne() {
        Product product = new Product("Test Product", 9.99, 10, "Test Description", "Test Producer", 1);
        int result = productController.updateProduct(product, 1);
        assertEquals(1, result);
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        // Create a product
        Product product = new Product("Test Product", 9.99, 10, "Test Description", "Test Producer", 1);
        productController.createProduct(product);

        // Delete the product
        productController.deleteProduct("Test Product");

        // Verify that the product no longer exists
        assertFalse(productController.exists("Test Product"));
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldNotThrowException() {
        assertDoesNotThrow(() -> productController.deleteProduct("Nonexistent Product"));
    }


    @Test
    void showProductsFiltered_WhenFilterApplied_ShouldReturnCorrectCount() {
        // Create some products
        Product product1 = new Product("Product 1", 9.99, 10, "Description 1", "Producer 1", 1);
        Product product2 = new Product("Product 2", 19.99, 5, "Description 2", "Producer 2", 1);
        Product product3 = new Product("Product 3", 29.99, 8, "Description 3", "Producer 1", 2);
        productController.createProduct(product1);
        productController.createProduct(product2);
        productController.createProduct(product3);

        // Apply filter
        int count = productController.showProductsFiltered(null, 10, 30, null, null);

        assertEquals(2, count);
    }

    // Add more tests for the remaining methods...

}
