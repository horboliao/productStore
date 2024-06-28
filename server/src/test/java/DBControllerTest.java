import clientServerApp.controllers.DBController;
import clientServerApp.entity.Group;
import clientServerApp.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DBControllerTest {
    private DBController dbController;

    @BeforeEach
    void setup() {
        // Set up the database connection and initialize the DBController
        // You can use a mocking framework or an in-memory database for testing purposes
        // Here, we assume the connection and controller are properly initialized
        String fileName = ":memory:"; // Use an in-memory database for testing
        dbController = new DBController(fileName);
    }

    @Test
    void createGroup_WhenGroupDoesNotExist_ReturnsPositiveId() {
        // Arrange
        Group group = new Group("Test Group", "Test Description");

        // Act
        int result = dbController.createGroup(group);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    void createGroup_WhenGroupExists_ReturnsNegativeId() {
        // Arrange
        Group group = new Group("Existing Group", "Test Description");
        dbController.createGroup(group);

        // Act
        int result = dbController.createGroup(group);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    void createProduct_WhenProductDoesNotExist_ReturnsPositiveId() {
        // Arrange
        Product product = new Product("Test Product", 10.0, 5, "Test Description", "Test Producer", 1);

        // Act
        int result = dbController.createProduct(product);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    void createProduct_WhenProductExists_ReturnsNegativeId() {
        // Arrange
        Product product = new Product("Existing Product", 10.0, 5, "Test Description", "Test Producer", 1);
        dbController.createProduct(product);

        // Act
        int result = dbController.createProduct(product);

        // Assert
        assertTrue(result < 0);
    }

    // Write more tests for other methods in the DBController class
    // Remember to include positive and negative test cases for different scenarios
}
