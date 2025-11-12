package proyectoDesarrollo.controllers;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.services.DatabaseService;

public class UserController {

    @FXML
    private TableColumn<User, String> addressColumn;

    @FXML
    private TextField addressInput;

    @FXML
    private Button buttonFilter;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonNew;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TextField emailInput;

    @FXML
    private TableColumn<User, String> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TextField nameInput;

    @FXML
    private TableColumn<User, String> phoneColumn;

    @FXML
    private TextField phoneInput;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TextField roleInput;

    @FXML
    private TableView<User> userTable;

    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadUsers();
    }

    private void loadUsers() {
    try {
        DatabaseService db = DatabaseService.getInstance();
        ObservableList<User> users = db.getAllUsers();
        userTable.setItems(users);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @FXML
    void buttonFilterOnAction(ActionEvent event) {

    }

    @FXML
    void buttonDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void buttonNewOnAction(ActionEvent event) {

    }

    @FXML
    void buttonUpdateOnAction(ActionEvent event) {

    }

}
