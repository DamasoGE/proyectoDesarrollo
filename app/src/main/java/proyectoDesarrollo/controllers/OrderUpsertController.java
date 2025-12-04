package proyectoDesarrollo.controllers;

import java.io.IOException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.interfaz.controllers.OrderController;
import proyectoDesarrollo.models.Order;
import proyectoDesarrollo.models.Service;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.utils.AppState;

public class OrderUpsertController {

    private Order order;

    @FXML
    private DatePicker appointmentInput;
    @FXML
    private Button button;
    @FXML
    private Button customerButton;
    @FXML
    private Button serviceButton;
    @FXML
    private TextField locationInput;
    @FXML
    private Spinner<Integer> hourInput;
    @FXML
    private Spinner<Integer> minuteInput;
    @FXML
    private TextField notesInput;
    @FXML
    private Spinner<Integer> participantsInput;
    @FXML
    private TextField priceFinalInput;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private Text textCustomer;
    @FXML
    private Text textService;
    @FXML
    private Text titleUpsertText;

    public void initialize() {

        AppState appState = AppState.getInstance();
        order = appState.getSelectedOrder();

        // Spinner para participantes
        participantsInput.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        participantsInput.setEditable(true);

        // Spinner para hora y minuto
        hourInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        hourInput.setEditable(true);

        minuteInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        minuteInput.setEditable(true);

        // Status
        statusChoiceBox.getItems().addAll("pending", "confirmed", "canceled", "completed");

        if (order != null) {
            titleUpsertText.setText("UPDATE ORDER: " + order.getId());

            notesInput.setText(order.getNotes());
            locationInput.setText(order.getLocation());

            participantsInput.getValueFactory().setValue(
                    order.getParticipants() != null ? order.getParticipants() : 1);

            statusChoiceBox.setValue(
                    order.getStatus() != null ? order.getStatus() : "pending");

            if (order.getPriceFinal() != null) {
                priceFinalInput.setText(String.valueOf(order.getPriceFinal()));
            }

            if (order.getAppointment() != null) {
                LocalDate date = order.getAppointment().toLocalDateTime().toLocalDate();
                appointmentInput.setValue(date);

                hourInput.getValueFactory().setValue(order.getAppointment().toLocalDateTime().getHour());
                minuteInput.getValueFactory().setValue(order.getAppointment().toLocalDateTime().getMinute());
            }

            if (order.getCustomerName() != null) {
                textCustomer.setText(order.getCustomerName());
            } else if (order.getCustomerId() != null) {
                textCustomer.setText("User ID: " + order.getCustomerId());
            } else {
                textCustomer.setText("No user selected");
            }

            if (order.getServiceName() != null) {
                textService.setText(order.getServiceName());
            } else if (order.getServiceId() != null) {
                textService.setText("Service ID: " + order.getServiceId());
            } else {
                textService.setText("No service selected");
            }

            button.setText("UPDATE");

        } else {
            titleUpsertText.setText("CREATE ORDER");

            statusChoiceBox.setValue("pending");
            participantsInput.getValueFactory().setValue(1);
            hourInput.getValueFactory().setValue(12);
            minuteInput.getValueFactory().setValue(0);

            textCustomer.setText("No user selected");
            textService.setText("No service selected");

            button.setText("CREATE");
        }
    }

    @FXML
    void buttonOnAction(ActionEvent event) {

        clearStyles();
        boolean valid = true;
        StringBuilder errors = new StringBuilder();

        AppState appState = AppState.getInstance();

        if (order == null) {
            order = new Order();
        }

        // Validaciones
        if (order.getCustomerId() == null) {
            textCustomer.setStyle("-fx-fill: red;");
            errors.append("Customer is required.\n");
            valid = false;
        }

        if (order.getServiceId() == null) {
            textService.setStyle("-fx-fill: red;");
            errors.append("Service is required.\n");
            valid = false;
        }

        if (appointmentInput.getValue() == null) {
            appointmentInput.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
            errors.append("Appointment date is required.\n");
            valid = false;
        }

        if (priceFinalInput.getText().isEmpty()) {
            priceFinalInput.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
            errors.append("Price is required.\n");
            valid = false;
        }

        Double price = null;
        if (!priceFinalInput.getText().isEmpty()) {
            try {
                price = Double.parseDouble(priceFinalInput.getText());
            } catch (NumberFormatException e) {
                priceFinalInput.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
                errors.append("Price must be a valid number.\n");
                valid = false;
            }
        }

        if (locationInput.getText().isEmpty()) {
            locationInput.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
            errors.append("Location is required.\n");
            valid = false;
        }

        if (!valid) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid data");
            alert.setHeaderText("Please fix the errors");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return;
        }

        // Set values
        order.setNotes(notesInput.getText());
        order.setLocation(locationInput.getText());
        order.setParticipants(participantsInput.getValue());
        order.setPriceFinal(price);
        order.setStatus(statusChoiceBox.getValue());

        LocalDate date = appointmentInput.getValue();
        Integer hour = hourInput.getValue();
        Integer minute = minuteInput.getValue();

        if (date != null && hour != null && minute != null) {
            order.setAppointment(java.sql.Timestamp.valueOf(date.atTime(hour, minute)));
        }

        // SAVE or UPDATE
        if (order.getId() == null) {
            OrderController.addOrder(order);
        } else {
            OrderController.updateOrder(order);
        }

        appState.setSelectedOrder(null);
        appState.setSelectedUser(null);
        appState.setSelectedService(null);

        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    @FXML
    void customerButtonOnAction(ActionEvent event) {

        AppState appState = AppState.getInstance();

        if (order == null) {
            order = new Order();
            appState.setSelectedOrder(order);
        }

        openModal("/UserView.fxml", "Select User");

        if (appState.getSelectedUser() != null) {
            User user = appState.getSelectedUser();
            order.setCustomerId(user.getId());
            order.setCustomerName(user.getUsername());
            textCustomer.setText(user.getUsername());
        }
    }

    @FXML
    void serviceButtonOnAction(ActionEvent event) {

        AppState appState = AppState.getInstance();

        if (order == null) {
            order = new Order();
            appState.setSelectedOrder(order);
        }

        openModal("/ServiceView.fxml", "Select Service");

        if (appState.getSelectedService() != null) {
            Service service = appState.getSelectedService();
            order.setServiceId(service.getId());
            order.setServiceName(service.getName());
            textService.setText(service.getName());
        }
    }

    private void clearStyles() {
        appointmentInput.setStyle(null);
        priceFinalInput.setStyle(null);
        locationInput.setStyle(null);
        textCustomer.setStyle(null);
        textService.setStyle(null);
    }

    private void openModal(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof UserControllerView userController) {
                userController.setMode(UserControllerView.Mode.SELECT_ONLY);
                userController.setupMode();
            }

            if (controller instanceof ServiceControllerView serviceController) {
                serviceController.setMode(ServiceControllerView.Mode.SELECT_ONLY);
                serviceController.setupMode();
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
