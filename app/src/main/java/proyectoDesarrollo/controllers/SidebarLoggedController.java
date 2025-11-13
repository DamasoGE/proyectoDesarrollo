package proyectoDesarrollo.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.MainController;
import proyectoDesarrollo.utils.AppState;

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


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
    AppState state = AppState.getInstance();
    
    labelUser.setText(state.getUsername());
    labelRole.setText(state.getRole());

    state.usernameProperty().addListener((obs, oldVal, newVal) -> {
        labelUser.setText(newVal != null ? newVal : "");
    });

    state.roleProperty().addListener((obs, oldVal, newVal) -> {
        labelRole.setText(newVal != null ? newVal : "");
    });
    }

    @FXML
    void buttonExitOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConfirmExitView.fxml"));
            VBox root = loader.load();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Exit Confirmation");
            modal.setScene(new Scene(root));
            modal.showAndWait();
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
    }
}
