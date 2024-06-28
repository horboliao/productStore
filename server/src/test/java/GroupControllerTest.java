import clientServerApp.controllers.GroupController;
import clientServerApp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GroupControllerTest {
    private GroupController groupController;

    @BeforeEach
    void setup() {
        String fileName = ":memory:"; // Use an in-memory database for testing
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);

            // Create the necessary tables
            PreparedStatement groupTableSt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS 'groups' (" +
                    "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "'name' VARCHAR(50) NOT NULL, " +
                    "'description' VARCHAR(300) NOT NULL);");
            groupTableSt.executeUpdate();

            // Initialize the GroupController
            groupController = new GroupController(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void createGroup_WhenGroupDoesNotExist_ReturnsPositiveId() {
        // Arrange
        Group group = new Group("Test Group", "Test Description");

        // Act
        int result = groupController.createGroup(group);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    void createGroup_WhenGroupExists_ReturnsNegativeId() {
        // Arrange
        Group group = new Group("Existing Group", "Test Description");
        groupController.createGroup(group);

        // Act
        int result = groupController.createGroup(group);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    void getGroupId_WhenGroupExists_ReturnsGroupId() {
        // Arrange
        Group group = new Group("Test Group", "Test Description");
        groupController.createGroup(group);

        // Act
        Integer result = groupController.getGroupId("Test Group");

        // Assert
        assertNotNull(result);
    }

    @Test
    void getGroupId_WhenGroupDoesNotExist_ReturnsNull() {
        // Act
        Integer result = groupController.getGroupId("Nonexistent Group");

        // Assert
        assert result ==0;
    }

    // Write more tests for other methods in the GroupController class
    // Remember to include positive and negative test cases for different scenarios
}
