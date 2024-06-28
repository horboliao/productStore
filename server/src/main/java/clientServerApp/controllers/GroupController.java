package clientServerApp.controllers;

import clientServerApp.entity.Group;

import java.sql.*;

public class GroupController {
    private static Connection connection;

    public GroupController(Connection connection){
        this.connection = connection;
    }

    public int createGroup(Group group){
        if (exists(group.getName())) return -1;
        try {
            PreparedStatement st = connection.prepareStatement(
                    "INSERT INTO groups (name, description) " +
                            "values (?, ?)");

            st.setString(1, group.getName());
            st.setString(2, group.getDescription());
            st.execute();
            return 1;
        } catch (SQLException e) {
            System.out.println("Couldn't add group " + group.getName());
            e.printStackTrace();
        }
        return -1;
    }

    public String showGroups(){
        try{
            String groups = "[";
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM groups");
            while (res.next()) {
                String json = "{\"id\":\"" + res.getInt("id") +
                        "\", \"name\":\"" + res.getString("name") +
                        "\", \"description\":\"" + res.getString("description") + "\"}";
                groups += json + ", ";
            }
            if(groups.length() > 1)
                groups = groups.substring(0, groups.length()-2);
            groups += "]";
            System.out.println(groups);
            res.close();
            st.close();
            return groups;
        }catch(SQLException e){
            System.out.println("Couldn't show groups.");
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getGroupId(String name){
        try(Statement statement = connection.createStatement()){
            String sql = String.format("SELECT * FROM groups WHERE name = '%s'", name);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                return id;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Can't get group id.");
            return null;
        }
    }

    public String getGroupName(Integer id) {
        try(Statement statement = connection.createStatement()){
            String sql = String.format("SELECT * FROM groups WHERE id = %s", id);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                return name;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Can't get group name.");
            return null;
        }
    }

    public boolean exists(String name){
        try (Statement statement = connection.createStatement()) {
            String query = String.format("SELECT * FROM groups WHERE name = '%s'", name);
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Couldn't check if group exists.");
            return false;
        }
    }

    boolean exists(Integer id){
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM groups WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Couldn't check if product exists.");
            return false;
        }
    }

    public int updateGroup(Group group, Integer id) {
        if (exists(group.getName()) && !group.getName().equals(getGroupName(id))) return -1;
        try (final PreparedStatement st = connection.prepareStatement("UPDATE groups SET name = ?, description = ? WHERE id = ?")) {
            st.setString(1, group.getName());
            st.setString(2, group.getDescription());
            st.setInt(3, id);
            st.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println("Couldn't update group " + group.getName());
        }
        return -1;
    }

    public void deleteGroup(String name){
        if (!exists(name)) return;

        Integer id = getGroupId(name);

        try(PreparedStatement st = connection.prepareStatement("DELETE FROM products WHERE group_id = ?")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't delete products.");
        }

        try(PreparedStatement st = connection.prepareStatement("DELETE FROM groups WHERE name = ?")) {
            st.setString(1, name);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't delete group " + name);
        }
    }

    public Group getGroup(Integer id) {
        String query = String.format("SELECT * FROM groups WHERE id = %d", id);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                return new Group(name, description);
            } else {
                System.out.println("No group found with id: " + id);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't get group: " + e.getMessage());
            return null;
        }
    }

}