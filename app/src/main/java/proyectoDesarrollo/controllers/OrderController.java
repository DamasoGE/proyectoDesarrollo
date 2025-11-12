package proyectoDesarrollo.controllers;

import java.time.LocalDateTime;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import proyectoDesarrollo.models.Order;
import proyectoDesarrollo.services.DatabaseService;

public class OrderController {

    @FXML
    private DatePicker appointmentMaxInput;

    @FXML
    private DatePicker appointmentMinInput;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonFilter;

    @FXML
    private Button buttonNew;

    @FXML
    private TextField customerUsernameInput;

    @FXML
    private TextField locationInput;

    @FXML
    private Spinner<Integer> maxParticipantsMaxInput;

    @FXML
    private Spinner<Integer> maxParticipantsaMinInput;

    @FXML
    private TextField maxPriceInput;

    @FXML
    private TextField minPriceInput;

    @FXML
    private TextField serviceNameInput;

    @FXML
    private TableColumn<Order, String> statusColumn;

    @FXML
    private TableColumn<Order, LocalDateTime> appointmentColumn;

    @FXML
    private TableColumn<Order, String> customerIdColumn;

    @FXML
    private TableColumn<Order, String> idColumn;

    @FXML
    private TableColumn<Order, String> locationColumn;

    @FXML
    private TableColumn<Order, String> notesColumn;

    @FXML
    private TableColumn<Order, Integer> participantsColumn;

    @FXML
    private TableColumn<Order, Integer> priceFinalColumn;

    @FXML
    private TableColumn<Order, String> serviceIdColumn;

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private ChoiceBox<String> statusBox;

    @FXML
    public void initialize() {
        // Configurar las columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        serviceIdColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        appointmentColumn.setCellValueFactory(new PropertyValueFactory<>("appointment"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        priceFinalColumn.setCellValueFactory(new PropertyValueFactory<>("priceFinal"));
        participantsColumn.setCellValueFactory(new PropertyValueFactory<>("participants"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        // Cargar datos
        loadOrders();
    }

    private void loadOrders() {
        DatabaseService dbService;
        try {
            dbService = DatabaseService.getInstance();
            ObservableList<Order> orders = dbService.getAllOrders();
            orderTable.setItems(orders);
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
