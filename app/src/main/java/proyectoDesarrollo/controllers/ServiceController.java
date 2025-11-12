package proyectoDesarrollo.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import proyectoDesarrollo.models.Service;
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
        DatabaseService dbService;
        try {
            dbService = DatabaseService.getInstance();
            ObservableList<Service> services = dbService.getAllServices();
            serviceTable.setItems(services);
        } catch (Exception e) {
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

    }

    @FXML
    void buttonUpdateOnAction(ActionEvent event) {

    }

}
