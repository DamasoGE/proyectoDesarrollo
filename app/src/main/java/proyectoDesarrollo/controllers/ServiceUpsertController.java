package proyectoDesarrollo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import proyectoDesarrollo.interfaz.controllers.ServiceController;
import proyectoDesarrollo.models.Service;
import proyectoDesarrollo.utils.AppState;

public class ServiceUpsertController {

    @FXML private ToggleGroup activeToggleGroup;
    @FXML private Button button;
    @FXML private TextField descriptionInput;
    @FXML private Spinner<Integer> durationInput;
    @FXML private Spinner<Integer> maxParticipantsInput;
    @FXML private TextField nameInput;
    @FXML private TextField priceInput;
    @FXML private RadioButton radioButtonNo;
    @FXML private RadioButton radioButtonYes;
    @FXML private Text titleUpsertText;

    private Service service;

    @FXML
    void buttonOnAction(ActionEvent event) {
        clearStyles();

        boolean valid = true;
        StringBuilder errorMsg = new StringBuilder();

        if (nameInput.getText().isEmpty()) {
            nameInput.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
            errorMsg.append("Name can't be empty.\n");
            valid = false;
        }

        try {
            double price = Double.parseDouble(priceInput.getText());
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            priceInput.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
            errorMsg.append("Price must be a positive number.\n");
            valid = false;
        }

        if (!valid) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect Input");
            alert.setHeaderText("Correct the errors before continuing");
            alert.setContentText(errorMsg.toString());
            alert.showAndWait();
            return;
        }

        boolean isActive = activeToggleGroup.getSelectedToggle() == radioButtonYes;

        if (service == null) {
            service = new Service(
                    nameInput.getText(),
                    descriptionInput.getText(),
                    Double.parseDouble(priceInput.getText()),
                    durationInput.getValue(),
                    maxParticipantsInput.getValue(),
                    isActive
            );
            ServiceController.addService(service);
        } else {
            service.setName(nameInput.getText());
            service.setDescription(descriptionInput.getText());
            service.setPrice(Double.parseDouble(priceInput.getText()));
            service.setDuration(durationInput.getValue());
            service.setMaxParticipants(maxParticipantsInput.getValue());
            service.setActive(isActive);

            ServiceController.updateService(service);
        }

        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    private void clearStyles() {
        nameInput.setStyle(null);
        priceInput.setStyle(null);
    }

    public void initialize() {
        AppState appState = AppState.getInstance();
        service = appState.getSelectedService();

        // Inicializar Spinners
        SpinnerValueFactory.IntegerSpinnerValueFactory durationFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1440, 0);
        durationInput.setValueFactory(durationFactory);
        durationInput.setEditable(true);

        SpinnerValueFactory.IntegerSpinnerValueFactory maxParticipantsFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        maxParticipantsInput.setValueFactory(maxParticipantsFactory);
        maxParticipantsInput.setEditable(true);

        if (service != null) {
            titleUpsertText.setText("UPDATE SERVICE: " + service.getId());

            nameInput.setText(service.getName());
            descriptionInput.setText(service.getDescription());
            durationInput.getValueFactory().setValue(service.getDuration());
            maxParticipantsInput.getValueFactory().setValue(service.getMaxParticipants());
            priceInput.setText(String.valueOf(service.getPrice()));

            if (service.isActive()) {
                activeToggleGroup.selectToggle(radioButtonYes);
            } else {
                activeToggleGroup.selectToggle(radioButtonNo);
            }

        } else {
            titleUpsertText.setText("CREATE SERVICE");
            activeToggleGroup.selectToggle(radioButtonNo);
        }
    }
}
