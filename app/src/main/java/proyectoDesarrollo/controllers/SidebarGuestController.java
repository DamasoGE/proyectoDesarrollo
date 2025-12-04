package proyectoDesarrollo.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SidebarGuestController {

    @FXML
    private Button buttonExit;

    @FXML
    void buttonExitOnAction() {
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

}
