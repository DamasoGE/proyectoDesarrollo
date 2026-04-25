package proyectoDesarrollo.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.MainController;
import proyectoDesarrollo.utils.AppState;
import proyectoDesarrollo.utils.ImageToText;

public class SidebarLoggedController {

    private MainController mainController;

    @FXML
    private Button buttonExit;

    @FXML
    private Text labelRole;

    @FXML
    private Text labelUser;

    @FXML
    private Button ordersButton;

    @FXML
    private Button servicesButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button usersButton;

    @FXML
    private ImageView imageViewUser;

    @FXML
    private BorderPane sidebarPane;

    private Map<Button, String> buttonColors;

    @FXML
    private Button activeButton;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {

        buttonColors = new HashMap<>();
        buttonColors.put(ordersButton, "#F42E5F");
        buttonColors.put(servicesButton, "#F1C40F");
        buttonColors.put(usersButton, "#4E9BAB");
        buttonColors.put(profileButton, "#2F72A9");

        AppState state = AppState.getInstance();

        // Suscribirse a cambios del currentUser
        state.currentUserProperty().addListener((obs, oldUser, newUser) -> {
            if (newUser != null) {
                labelUser.setText(newUser.getUsername());
                labelRole.setText(newUser.getRole());

                String photoBase64 = newUser.getImage();

                if (photoBase64 != null && !photoBase64.isBlank()) {
                    try {
                        Image fxImage = ImageToText.base64ToFxImage(photoBase64);
                        imageViewUser.setImage(fxImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    imageViewUser.setImage(null);
                }
            } else {
                labelUser.setText("");
                labelRole.setText("");
            }
        });

        // Inicial
        if (state.getCurrentUser() != null) {

            labelUser.setText(state.getCurrentUser().getUsername());
            labelRole.setText(state.getCurrentUser().getRole());

            String photoBase64 = state.getCurrentUser().getImage();

            try {
                if (photoBase64 != null && !photoBase64.isBlank()) {
                    imageViewUser.setImage(ImageToText.base64ToFxImage(photoBase64));
                } else {
                    imageViewUser.setImage(
                            new Image(getClass().getResource("/images/default_user.png").toExternalForm()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            labelUser.setText("");
            labelRole.setText("");
        }

        setActiveButton(profileButton);
    }

    @FXML
    void buttonExitOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConfirmView.fxml"));
            VBox root = loader.load();

            ConfirmController confirmController = loader.getController();
            confirmController.setMessage("Do you really want to exit?");

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Exit Confirmation");
            modal.setScene(new Scene(root));
            modal.showAndWait();

            if (confirmController.isConfirmed()) {

                Platform.exit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ordersButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrderView.fxml"));
            Node ordersContent = loader.load();
            mainController.setRightContent(ordersContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setActiveButton(ordersButton);
    }

    @FXML
    void servicesButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ServiceView.fxml"));
            Node servicesContent = loader.load();
            mainController.setRightContent(servicesContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setActiveButton(servicesButton);
    }

    @FXML
    void usersButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserView.fxml"));
            Node usersContent = loader.load();
            mainController.setRightContent(usersContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setActiveButton(usersButton);
    }

    @FXML
    void profileButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileView.fxml"));
            Node usersContent = loader.load();
            mainController.setRightContent(usersContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setActiveButton(profileButton);
    }

    private void setActiveButton(Button button) {
        ordersButton.getStyleClass().remove("active-button");
        servicesButton.getStyleClass().remove("active-button");
        usersButton.getStyleClass().remove("active-button");
        profileButton.getStyleClass().remove("active-button");

        button.getStyleClass().add("active-button");
        activeButton = button;

        String color = buttonColors.get(button);
        sidebarPane.setStyle("-fx-background-color: " + color + ";");
    }

}
