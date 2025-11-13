package proyectoDesarrollo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ServiceUpsertController {

    @FXML
    private ToggleGroup activeToggleGroup;

    @FXML
    private Button button;

    @FXML
    private TextField descriptionInput;

    @FXML
    private TextField durationInput;

    @FXML
    private TextField maxParticipantsInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField priceInput;

    @FXML
    private RadioButton radioButtonNo;

    @FXML
    private RadioButton radioButtonYes;

    @FXML
    void buttonOnAction(ActionEvent event) {

    }

}
