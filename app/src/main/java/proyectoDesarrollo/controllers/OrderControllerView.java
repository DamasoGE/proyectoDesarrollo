package proyectoDesarrollo.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.interfaz.controllers.OrderController;
import proyectoDesarrollo.models.Order;
import proyectoDesarrollo.utils.AppState;

public class OrderControllerView {

    private ObservableList<Order> allOrders;

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
    private Button buttonUpdate;

    @FXML
    private Button clearFilterButton;
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
    private TableColumn<Order, Timestamp> appointmentColumn;
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
    private TableColumn<Order, String> customerColumn;
    @FXML
    private TableColumn<Order, String> serviceColumn;

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private ChoiceBox<String> statusBox;

    @FXML
    public void initialize() {

        // Spinners
        maxParticipantsaMinInput.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));

        maxParticipantsMaxInput.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 100));

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

        MenuItem exportItem = new MenuItem("Export to CSV");
        exportItem.setOnAction(e -> exportTableToCSV());

        contextMenu.getItems().addAll(editItem, deleteItem, exportItem);

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
            loadingStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
            loadingStage.setTitle("Loading...");
            loadingStage.show();

            new Thread(() -> {
                ObservableList<Order> orders = OrderController.getAllOrders();
                allOrders = FXCollections.observableArrayList(orders);
                Platform.runLater(() -> {
                    orderTable.setItems(allOrders);
                    loadingStage.close();
                });
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonFilterOnAction(ActionEvent event) {
        if (allOrders == null)
            return;

        ObservableList<Order> filtered = allOrders.filtered(order -> {

            String serviceName = serviceNameInput.getText().toLowerCase().trim();
            String customerUsername = customerUsernameInput.getText().toLowerCase().trim();
            String location = locationInput.getText().toLowerCase().trim();
            String minPriceText = minPriceInput.getText().trim();
            String maxPriceText = maxPriceInput.getText().trim();
            String status = statusBox.getValue();

            Integer minPrice = null;
            Integer maxPrice = null;

            try {
                minPrice = minPriceText.isEmpty() ? null : Integer.parseInt(minPriceText);
            } catch (NumberFormatException ignored) {
            }
            try {
                maxPrice = maxPriceText.isEmpty() ? null : Integer.parseInt(maxPriceText);
            } catch (NumberFormatException ignored) {
            }

            Timestamp minDate = null;
            Timestamp maxDate = null;

            LocalDate minLocal = appointmentMinInput.getValue();
            LocalDate maxLocal = appointmentMaxInput.getValue();

            if (minLocal != null) {
                minDate = Timestamp.valueOf(minLocal.atStartOfDay());
            }
            if (maxLocal != null) {
                maxDate = Timestamp.valueOf(maxLocal.atTime(23, 59, 59));
            }

            int minParticipants = maxParticipantsaMinInput.getValue();
            int maxParticipants = maxParticipantsMaxInput.getValue();

            boolean matchesService = serviceName.isEmpty()
                    || order.getServiceName().toLowerCase().contains(serviceName);
            boolean matchesCustomer = customerUsername.isEmpty()
                    || order.getCustomerName().toLowerCase().contains(customerUsername);
            boolean matchesLocation = location.isEmpty() || order.getLocation().toLowerCase().contains(location);

            boolean matchesPrice = true;
            if (minPrice != null)
                matchesPrice = order.getPriceFinal() >= minPrice;
            if (maxPrice != null)
                matchesPrice &= order.getPriceFinal() <= maxPrice;

            boolean matchesStatus = status == null || status.isEmpty() || order.getStatus().equalsIgnoreCase(status);

            boolean matchesDate = true;
            if (minDate != null)
                matchesDate = !order.getAppointment().before(minDate);
            if (maxDate != null)
                matchesDate &= !order.getAppointment().after(maxDate);

            boolean matchesParticipants = order.getParticipants() >= minParticipants
                    && order.getParticipants() <= maxParticipants;

            return matchesService && matchesCustomer && matchesLocation &&
                    matchesPrice && matchesStatus && matchesDate && matchesParticipants;
        });

        orderTable.setItems(filtered);
    }

    @FXML
    void clearFilterButtonOnAction(ActionEvent event) {
        serviceNameInput.clear();
        customerUsernameInput.clear();
        locationInput.clear();
        minPriceInput.clear();
        maxPriceInput.clear();
        statusBox.setValue(null);
        appointmentMinInput.setValue(null);
        appointmentMaxInput.setValue(null);
        maxParticipantsaMinInput.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));

        maxParticipantsMaxInput.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 100));

        if (allOrders != null) {
            orderTable.setItems(allOrders);
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

        if (selected == null)
            return;

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

    private void exportTableToCSV() {
        if (orderTable.getItems().isEmpty()) {
            System.out.println("No hay datos para exportar");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(orderTable.getScene().getWindow());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Cabecera
                writer.write(
                        "ID,Customer,CustomerId,Service,ServiceId,Appointment,Status,Notes,PriceFinal,Participants,Location");
                writer.newLine();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Filas
                for (Order order : orderTable.getItems()) {
                    Timestamp ts = order.getAppointment();
                    String appointmentStr = ts != null ? ts.toLocalDateTime().format(formatter) : "";

                    String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%d,%s",
                            order.getId(),
                            order.getCustomerName(),
                            order.getCustomerId(),
                            order.getServiceName(),
                            order.getServiceId(),
                            appointmentStr,
                            order.getStatus(),
                            order.getNotes() != null ? order.getNotes() : "",
                            order.getPriceFinal(),
                            order.getParticipants(),
                            order.getLocation() != null ? order.getLocation() : "");
                    writer.write(line);
                    writer.newLine();
                }

                System.out.println("CSV exportado correctamente: " + file.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
