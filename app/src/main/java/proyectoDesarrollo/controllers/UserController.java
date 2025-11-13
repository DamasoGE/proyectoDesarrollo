package proyectoDesarrollo.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoadingView.fxml"));
            Parent loadingRoot = loader.load();

            Stage loadingStage = new Stage();
            loadingStage.initModality(Modality.APPLICATION_MODAL);
            loadingStage.setScene(new Scene(loadingRoot));
            loadingStage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/icon.png")));
            loadingStage.setTitle("Cargando...");
            loadingStage.show();

            new Thread(() -> {
                try {
                    DatabaseService db = DatabaseService.getInstance();
                    ObservableList<User> users = db.getAllUsers();

                    Platform.runLater(() -> {
                        userTable.setItems(users);
                        loadingStage.close();
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                    Platform.runLater(loadingStage::close);
                }
            }).start();

        } catch (IOException e) {
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
        openModal("/UserUpsertView.fxml", "Nuevo Usuario");
    }

    @FXML
    void buttonUpdateOnAction(ActionEvent event) {
        openModal("/UserUpsertView.fxml", "Actualizar Usuario");
    }

    private void openModal(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/icon.png")));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
