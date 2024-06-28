import clientServerApp.entity.Group;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupTest {

    @Test
    void toJSON_ShouldReturnValidJSON() {
        Group group = new Group("Group Name", "Group Description");

        JSONObject expectedJson = new JSONObject();
        expectedJson.put("name", "Group Name");
        expectedJson.put("description", "Group Description");

        assertEquals(expectedJson.toString(), group.toJSON().toString());
    }

    @Test
    void getName_ShouldReturnGroupName() {
        Group group = new Group("Group Name", "Group Description");
        assertEquals("Group Name", group.getName());
    }

    @Test
    void setName_ShouldUpdateGroupName() {
        Group group = new Group("Group Name", "Group Description");
        group.setName("New Group Name");
        assertEquals("New Group Name", group.getName());
    }

    @Test
    void getDescription_ShouldReturnGroupDescription() {
        Group group = new Group("Group Name", "Group Description");
        assertEquals("Group Description", group.getDescription());
    }

    @Test
    void setDescription_ShouldUpdateGroupDescription() {
        Group group = new Group("Group Name", "Group Description");
        group.setDescription("New Group Description");
        assertEquals("New Group Description", group.getDescription());
    }

}
