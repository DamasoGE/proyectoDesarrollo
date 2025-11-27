package proyectoDesarrollo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import proyectoDesarrollo.interfaz.controllers.UserController;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.utils.AppState;

public class UserUpsertController {

    private User user;

    @FXML
    private TextField addressInput;

    @FXML
    private Button button;

    @FXML
    private TextField emailInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField phoneInput;

    @FXML
    private ComboBox<String> roleInput;

    @FXML
    private Text titleUpsertText;

    @FXML
    void buttonOnAction(ActionEvent event) {
        
        clearStyles();

        boolean valid = true;
        StringBuilder errorMsg = new StringBuilder();


        if (!isValidEmail(emailInput.getText())) {
            emailInput.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
            errorMsg.append("Not valid email.\n");
            valid = false;
        }

        if (nameInput.getText().isEmpty()) {
            nameInput.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
            errorMsg.append("Name can't be empty.\n");
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

        if (user == null) {
            user = new User(
                    nameInput.getText(),
                    emailInput.getText(),
                    roleInput.getValue(),
                    phoneInput.getText(),
                    addressInput.getText(),
                    "");
            UserController.addUser(user);

        } else {
            user.setUsername(nameInput.getText());
            user.setEmail(emailInput.getText());
            user.setAddress(addressInput.getText());
            user.setPhone(phoneInput.getText());
            user.setRole(roleInput.getValue());

            UserController.updateUser(user);
        }

        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
    }

    private void clearStyles() {
        emailInput.setStyle(null);
        nameInput.setStyle(null);
    }

    public void initialize() {
        roleInput.getItems().addAll("admin", "worker", "customer");
        roleInput.setValue("customer");

        AppState appState = AppState.getInstance();
        user = appState.getSelectedUser();

        if (user != null) {
            titleUpsertText.setText("UPDATE USER: " + user.getId());

            nameInput.setText(user.getUsername());
            emailInput.setText(user.getEmail());
            addressInput.setText(user.getAddress());
            phoneInput.setText(user.getPhone());
            roleInput.setValue(user.getRole());

        } else {
            titleUpsertText.setText("CREATE USER");
        }
    }
}
