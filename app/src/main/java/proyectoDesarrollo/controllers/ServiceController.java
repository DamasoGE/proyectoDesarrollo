package proyectoDesarrollo.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.models.Service;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.services.DatabaseService;

public class ServiceController {

    @FXML
    private ToggleGroup activeToggleGroup;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonFilter;

    @FXML
    private Button buttonNew;

    @FXML
    private TableView<Service> serviceTable;

    @FXML
    private TableColumn<Service, Boolean> activeColumn;

    @FXML
    private TableColumn<Service, String> descriptionColumn;

    @FXML
    private TableColumn<Service, Integer> durationColumn;

    @FXML
    private TableColumn<Service, String> idColumn;

    @FXML
    private TableColumn<Service, Integer> maxParticipantsColumn;

    @FXML
    private TableColumn<Service, String> nameColumn;

    @FXML
    private TableColumn<Service, Double> priceColumn;

    @FXML
    private Spinner<Integer> durationMaxInput;

    @FXML
    private Spinner<Integer> durationMinInput;

    @FXML
    private Spinner<Integer> maxParticipantsMinInput;

    @FXML
    private Spinner<Integer> maxParticipantsMaxInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField priceMinInput;

    @FXML
    private TextField priceMaxInput;

    @FXML
    private RadioButton radioButtonBoth;

    @FXML
    private RadioButton radioButtonNo;

    @FXML
    private RadioButton radioButtonYes;

    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        maxParticipantsColumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        activeColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isActive()));

        loadServices();
    }

    private void loadServices() {
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
                    ObservableList<Service> services = db.getAllServices();

                    Platform.runLater(() -> {
                        serviceTable.setItems(services);
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
    void buttonDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void buttonFilterOnAction(ActionEvent event) {

    }

    @FXML
    void buttonNewOnAction(ActionEvent event) {
        openModal("/ServiceUpsertView.fxml", "Actualizar Servicio");
    }

    @FXML
    void buttonUpdateOnAction(ActionEvent event) {
        openModal("/ServiceUpsertView.fxml", "Actualizar Servicio");
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
