package proyectoDesarrollo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.services.DatabaseService;
import proyectoDesarrollo.utils.AppState;

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
        DatabaseService db = DatabaseService.getInstance();
        User user = db.login(username, password);

        if (user != null) {
            System.out.println("Login correcto!");
            AppState state = AppState.getInstance();
            state.setLoggedIn(true);
            state.setUsername(user.getName());
            state.setRole(user.getRole());
        } else {
            System.out.println("Usuario o contraseña incorrectos");
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
