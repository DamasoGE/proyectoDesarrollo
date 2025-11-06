package proyectoDesarrollo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import proyectoDesarrollo.services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private ImageView loginImage;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void buttonLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Rellena todos los campos");
            return;
        }

        try {
            // Obtener conexión singleton
            Connection conn = DatabaseService.getInstance().getConnection();

            // Consulta segura
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login correcto!");
            } else {
                System.out.println("Usuario o contraseña incorrectos");
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
