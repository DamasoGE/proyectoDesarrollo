package proyectoDesarrollo.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import proyectoDesarrollo.interfaz.controllers.AuthController;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.utils.AppState;

import java.io.IOException;

public class LoginControllerView {

    @FXML
    private Button loginButton;

    @FXML
    private ImageView loginImage;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private void initialize() {
        Platform.runLater(() -> playFadeAndScale(loginImage));
        usernameField.setOnAction(event -> tryLogin());
        passwordField.setOnAction(event -> tryLogin());
    }

    @FXML
    void loginButtonOnAction(ActionEvent event) {
        tryLogin();
    }

    private void tryLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Rellena todos los campos");
            return;
        }

        Stage loadingStage = showLoadingModal();

        new Thread(() -> {
            User user = AuthController.login(username, password);
            Platform.runLater(() -> {
                if (loadingStage != null)
                    loadingStage.close();
                if (user != null) {
                    AppState state = AppState.getInstance();
                    state.setCurrentUser(user);
                    state.setLoggedIn(true);
                } else {
                    System.out.println("Usuario o contraseña incorrectos");
                }
            });
        }).start();
    }

    private Stage showLoadingModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoadingView.fxml"));
            Parent loadingRoot = loader.load();

            Stage loadingStage = new Stage();
            loadingStage.initModality(Modality.APPLICATION_MODAL);
            loadingStage.setScene(new Scene(loadingRoot));
            loadingStage.setTitle("Cargando...");
            loadingStage.show();

            return loadingStage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void playFadeAndScale(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(750), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(750), node);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition animation = new ParallelTransition(fade, scale);
        animation.play();
    }
}
