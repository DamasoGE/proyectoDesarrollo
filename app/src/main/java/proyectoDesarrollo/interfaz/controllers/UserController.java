package proyectoDesarrollo.interfaz.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectoDesarrollo.interfaz.DatabaseService;
import proyectoDesarrollo.models.User;

public class UserController {

    public static boolean addUser(User user) {
        String sql = "INSERT INTO users (id, username, password, phone, email, address, role, image) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (user.getId() == null || user.getId().isBlank()) {
                user.setId(java.util.UUID.randomUUID().toString());
            }

            stmt.setString(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, "");
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getRole());
            stmt.setString(8, "");

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("role"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("image"));
                    user.setId(rs.getString("id"));
                    users.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET username=?, phone=?, email=?, address=?, role=?, image=?"
                + "WHERE id=?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getImage());
            stmt.setString(7, user.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


        public static boolean updateUserWithPassword(User user, String password) {
        String sql = "UPDATE users SET username=?, phone=?, email=?, address=?, role=?, image=?, password=?"
                + "WHERE id=?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getImage());
            stmt.setString(7, password);
            stmt.setString(8, user.getId());


            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id=?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static User getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE id=?";
        User user = null;

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("role"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("image"));
                    user.setId(rs.getString("id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
