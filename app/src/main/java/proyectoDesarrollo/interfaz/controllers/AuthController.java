package proyectoDesarrollo.interfaz.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import proyectoDesarrollo.interfaz.DatabaseService;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.utils.AppState;

public class AuthController {

    public static User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("role"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("image"));
                    user.setId(rs.getString("id"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean checkPassword(String userId, String password) {
        String sql = "SELECT password FROM users WHERE id = ?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    return password != null && password.equals(dbPassword);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void logout() {
        AppState appState = AppState.getInstance();
        appState.setCurrentUser(null);
    }

}
