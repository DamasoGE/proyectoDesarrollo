package proyectoDesarrollo.controllers;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.interfaz.controllers.UserController;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.utils.AppState;
import proyectoDesarrollo.utils.ImageToText;

public class ProfileController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button imageButton;

    @FXML
    private PasswordField confirmNewPasswordInput;

    @FXML
    private ImageView imageViewProfile;

    @FXML
    private Text labelRole;

    @FXML
    private Text labelUser;

    @FXML
    private Button logoutButton;

    @FXML
    private PasswordField newPasswordInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private TextField usernameInput;

    private String base64image;

    @FXML
    private void initialize() {
        AppState appState = AppState.getInstance();
        labelUser.setText(appState.getCurrentUser().getUsername());
        labelRole.setText(appState.getCurrentUser().getRole());

            try {
                imageViewProfile.setImage(ImageToText.base64ToFxImage(appState.getCurrentUser().getImage()));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @FXML
    void confirmButtonOnAction(ActionEvent event) {
        AppState appState = AppState.getInstance();
        var currentUser = appState.getCurrentUser();
        if (currentUser == null)
            return;

        String currentPassword = passwordInput.getText().trim();
        String newUsername = usernameInput.getText().trim();
        String newPassword = newPasswordInput.getText().trim();
        String confirmPassword = confirmNewPasswordInput.getText().trim();


        boolean hasChanges = (!newUsername.isEmpty() && !newUsername.equals(currentUser.getUsername())) // username
                                                                                                        // cambiado
                || (!newPassword.isEmpty()) // password cambiado
                || (base64image != null && !base64image.isEmpty()
                        && !base64image.equals(currentUser.getImage())); // imagen cambiada

        if (!hasChanges) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("No Changes");
            alert.setHeaderText(null);
            alert.setContentText("No changes detected to update.");
            alert.showAndWait();
            return;
        }

        // Validar contraseña actual usando AuthController
        if (!currentPassword.isEmpty() && !proyectoDesarrollo.interfaz.controllers.AuthController
                .checkPassword(currentUser.getId(), currentPassword)) {
            passwordInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Incorrect Password");
            alert.setHeaderText(null);
            alert.setContentText("The current password entered is incorrect.");
            alert.showAndWait();
            return;
        }
        passwordInput.setStyle(null);

        // Crear nuevo User para disparar listeners
        User updatedUser = new User(
                !newUsername.isEmpty() ? newUsername : currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getRole(),
                currentUser.getPhone(),
                currentUser.getAddress(),
                currentUser.getImage());
        updatedUser.setId(currentUser.getId());

        if (!newPassword.isEmpty() && newPassword.equals(confirmPassword)) {
            updatedUser.setPassword(newPassword);
        }

        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Password Mismatch");
            alert.setHeaderText(null);
            alert.setContentText("The new password and its confirmation do not match.");
            alert.showAndWait();
            newPasswordInput.clear();
            confirmNewPasswordInput.clear();
            return;
        }

        if (!base64image.isEmpty()) {
            updatedUser.setImage(base64image);
        }

        if (!newPassword.isEmpty()) {
            UserController.updateUserWithPassword(updatedUser, newPassword);
        } else {
            UserController.updateUser(updatedUser);
        }

        appState.setCurrentUser(updatedUser);

        labelUser.setText(updatedUser.getUsername());

        passwordInput.clear();
        newPasswordInput.clear();
        confirmNewPasswordInput.clear();
        base64image = null;

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Profile Updated");
        alert.setHeaderText(null);
        alert.setContentText("Your profile changes have been successfully saved!");
        alert.showAndWait();
    }

    @FXML
    void imageButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(imageButton.getScene().getWindow());
        if (selectedFile == null)
            return;

        try {
            // Convertir imagen a base64
            base64image = ImageToText.imageFileToBase64(selectedFile);

            // Mostrar imagen en el ImageView
            Image image = ImageToText.base64ToFxImage(base64image);
            imageViewProfile.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Image Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading or converting the selected image.");
            alert.showAndWait();
        }
    }

    @FXML
    void logoutButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConfirmView.fxml"));
            Parent root = loader.load();

            ConfirmController confirmController = loader.getController();
            confirmController.setMessage("Do you really want to log out?");

            Stage stage = new Stage();
            stage.setTitle("Confirm Logout");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (confirmController.isConfirmed()) {

                AppState state = AppState.getInstance();
                state.setSelectedUser(null);
                state.setSelectedOrder(null);
                state.setSelectedService(null);
                state.setLoggedIn(false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
