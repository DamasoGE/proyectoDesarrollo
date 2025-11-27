package proyectoDesarrollo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Label deleteText;

    private boolean confirmed = false;

    public void setMessage(String message) {
        deleteText.setText(message);
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        confirmed = false;
        closeStage(event);
    }

    @FXML
    void confirmButtonOnAction(ActionEvent event) {
        confirmed = true;
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
