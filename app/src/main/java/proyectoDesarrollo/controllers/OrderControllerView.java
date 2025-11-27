package proyectoDesarrollo.controllers;

import java.io.IOException;
import java.time.LocalDateTime;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.interfaz.controllers.OrderController;
import proyectoDesarrollo.models.Order;
import proyectoDesarrollo.utils.AppState;

public class OrderControllerView {

    @FXML private DatePicker appointmentMaxInput;
    @FXML private DatePicker appointmentMinInput;

    @FXML private Button buttonDelete;
    @FXML private Button buttonFilter;
    @FXML private Button buttonNew;
    @FXML private Button buttonUpdate;

    @FXML private TextField customerUsernameInput;
    @FXML private TextField locationInput;

    @FXML private Spinner<Integer> maxParticipantsMaxInput;
    @FXML private Spinner<Integer> maxParticipantsaMinInput;

    @FXML private TextField maxPriceInput;
    @FXML private TextField minPriceInput;

    @FXML private TextField serviceNameInput;

    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, LocalDateTime> appointmentColumn;
    @FXML private TableColumn<Order, String> customerIdColumn;
    @FXML private TableColumn<Order, String> idColumn;
    @FXML private TableColumn<Order, String> locationColumn;
    @FXML private TableColumn<Order, String> notesColumn;
    @FXML private TableColumn<Order, Integer> participantsColumn;
    @FXML private TableColumn<Order, Integer> priceFinalColumn;
    @FXML private TableColumn<Order, String> serviceIdColumn;
    @FXML private TableColumn<Order, String> customerColumn;
    @FXML private TableColumn<Order, String> serviceColumn;

    @FXML private TableView<Order> orderTable;
    @FXML private ChoiceBox<String> statusBox;

    @FXML
    public void initialize() {

        // Spinners
        maxParticipantsaMinInput.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0)
        );

        maxParticipantsMaxInput.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 100)
        );

        maxParticipantsaMinInput.setEditable(true);
        maxParticipantsMaxInput.setEditable(true);

        // Columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        serviceIdColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        appointmentColumn.setCellValueFactory(new PropertyValueFactory<>("appointment"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        priceFinalColumn.setCellValueFactory(new PropertyValueFactory<>("priceFinal"));
        participantsColumn.setCellValueFactory(new PropertyValueFactory<>("participants"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        // ChoiceBox estado
        statusBox.getItems().addAll("PENDING", "CONFIRMED", "CANCELLED", "COMPLETED");

        loadOrders();

        // Menú contextual
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Update");
        editItem.setOnAction(e -> {
            Order selected = orderTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openModal("/OrderUpsertView.fxml", "Update Order");
                loadOrders();
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> handleDelete());

        contextMenu.getItems().addAll(editItem, deleteItem);

        orderTable.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });

        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            buttonUpdate.setDisable(!hasSelection);
            buttonDelete.setDisable(!hasSelection);

            AppState.getInstance().setSelectedOrder(newSelection);
        });

        orderTable.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                if (!buttonUpdate.isFocused() && !buttonDelete.isFocused()) {
                    orderTable.getSelectionModel().clearSelection();
                }
            }
        });
    }

    private void loadOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoadingView.fxml"));
            Parent loadingRoot = loader.load();

            Stage loadingStage = new Stage();
            loadingStage.initModality(Modality.APPLICATION_MODAL);
            loadingStage.setScene(new Scene(loadingRoot));
            loadingStage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/icon.png")));
            loadingStage.setTitle("Loading...");
            loadingStage.show();

            new Thread(() -> {
                ObservableList<Order> orders = OrderController.getAllOrders();
                Platform.runLater(() -> {
                    orderTable.setItems(orders);
                    loadingStage.close();
                });
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonNewOnAction(ActionEvent event) {
        openModal("/OrderUpsertView.fxml", "Create Order");
        loadOrders();
    }

    @FXML
    void buttonUpdateOnAction(ActionEvent event) {
        openModal("/OrderUpsertView.fxml", "Update Order");
        loadOrders();
    }

    @FXML
    void buttonDeleteOnAction(ActionEvent event) {
        handleDelete();
    }

    private void handleDelete() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Order");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("This action cannot be undone");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                OrderController.deleteOrder(selected.getId());
                loadOrders();
            }
        });
    }

    @FXML
    void buttonFilterOnAction(ActionEvent event) {

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
