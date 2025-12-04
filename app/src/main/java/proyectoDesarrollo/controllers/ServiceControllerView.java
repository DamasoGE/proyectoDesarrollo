package proyectoDesarrollo.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.interfaz.controllers.ServiceController;
import proyectoDesarrollo.models.Service;
import proyectoDesarrollo.utils.AppState;

public class ServiceControllerView {

    public enum Mode {
        DEFAULT,
        SELECT_ONLY
    }

    private Mode mode = Mode.DEFAULT;
    private ObservableList<Service> allServices = FXCollections.observableArrayList();

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setupMode() {
        if (mode == Mode.SELECT_ONLY) {
            buttonNew.setManaged(false);
            buttonDelete.setManaged(false);
            buttonUpdate.setManaged(false);
            selectButton.setVisible(true);
        } else {
            buttonNew.setManaged(true);
            buttonDelete.setManaged(true);
            buttonUpdate.setManaged(true);
            selectButton.setVisible(false);
        }
    }

    @FXML
    private ToggleGroup activeToggleGroup;
    @FXML
    private Button buttonDelete, buttonFilter, buttonUpdate, buttonNew, selectButton;
    @FXML
    private Button clearFilterButton;
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
    private Spinner<Integer> durationMaxInput, durationMinInput, maxParticipantsMinInput, maxParticipantsMaxInput;
    @FXML
    private TextField nameInput, minPriceInput, maxPriceInput;
    @FXML
    private RadioButton radioButtonBoth, radioButtonNo, radioButtonYes;

    public void initialize() {
        AppState appState = AppState.getInstance();
        appState.setSelectedService(null);

        selectButton.setFocusTraversable(false);

        // Configuración de columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        maxParticipantsColumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        activeColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isActive()));

        // Configurar spinners
        durationMinInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1440, 0));
        durationMaxInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1440, 60));
        maxParticipantsMinInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        maxParticipantsMaxInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));

        durationMinInput.setEditable(true);
        durationMaxInput.setEditable(true);
        maxParticipantsMinInput.setEditable(true);
        maxParticipantsMaxInput.setEditable(true);

        loadServices();

        // Context menu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Update");
        editItem.setOnAction(e -> {
            Service selected = serviceTable.getSelectionModel().getSelectedItem();
            if (selected != null)
                openModal("/ServiceUpsertView.fxml", "Update Service");
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> buttonDeleteOnAction(null));

        contextMenu.getItems().addAll(editItem, deleteItem);

        serviceTable.setRowFactory(tv -> {
            TableRow<Service> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty())
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
            });
            return row;
        });

        serviceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            buttonUpdate.setDisable(!hasSelection);
            buttonDelete.setDisable(!hasSelection);

            AppState.getInstance().setSelectedService(newSelection);
        });

        serviceTable.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && mode != Mode.SELECT_ONLY) {
                if (!buttonUpdate.isFocused() && !buttonDelete.isFocused()) {
                    serviceTable.getSelectionModel().clearSelection();
                }
            }
        });

        setupMode();
    }

    private void loadServices() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoadingView.fxml"));
            Parent loadingRoot = loader.load();

            Stage loadingStage = new Stage();
            loadingStage.initModality(Modality.APPLICATION_MODAL);
            loadingStage.setScene(new Scene(loadingRoot));
            loadingStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
            loadingStage.setTitle("Cargando...");
            loadingStage.show();

            new Thread(() -> {
                try {
                    ObservableList<Service> services = ServiceController.getAllServices();
                    allServices = FXCollections.observableArrayList(services); // guardar copia completa

                    Platform.runLater(() -> {
                        serviceTable.setItems(allServices);
                        loadingStage.close();
                    });
                } catch (Exception e) {
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
        if (allServices == null)
            return; // Asegurarse de que hay servicios cargados

        ObservableList<Service> filtered = allServices.filtered(s -> {
            // Nombre
            String name = nameInput.getText().toLowerCase().trim();

            // Precio mínimo y máximo
            double min = 0, max = Double.MAX_VALUE;
            try {
                min = !minPriceInput.getText().isEmpty() ? Double.parseDouble(minPriceInput.getText()) : 0;
                max = !maxPriceInput.getText().isEmpty() ? Double.parseDouble(maxPriceInput.getText())
                        : Double.MAX_VALUE;
            } catch (NumberFormatException ignored) {
            }

            // Duración
            int durationMin = durationMinInput.getValue();
            int durationMax = durationMaxInput.getValue();

            // Máximo de participantes
            int maxPartMin = maxParticipantsMinInput.getValue();
            int maxPartMax = maxParticipantsMaxInput.getValue();

            // Filtro de activo
            Toggle selectedToggle = activeToggleGroup.getSelectedToggle();
            Boolean activeFilter = null;
            if (selectedToggle == radioButtonYes)
                activeFilter = true;
            else if (selectedToggle == radioButtonNo)
                activeFilter = false;

            // Comprobaciones de coincidencia
            boolean matchesName = name.isEmpty() || s.getName().toLowerCase().contains(name);
            boolean matchesPrice = s.getPrice() >= min && s.getPrice() <= max;
            boolean matchesDuration = s.getDuration() >= durationMin && s.getDuration() <= durationMax;
            boolean matchesParticipants = s.getMaxParticipants() >= maxPartMin && s.getMaxParticipants() <= maxPartMax;
            boolean matchesActive = (activeFilter == null) || s.isActive() == activeFilter;

            return matchesName && matchesPrice && matchesDuration && matchesParticipants && matchesActive;
        });

        serviceTable.setItems(filtered);
    }

    @FXML
    void buttonDeleteOnAction(ActionEvent event) {
        Service selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConfirmView.fxml"));
            Parent root = loader.load();
            ConfirmController controller = loader.getController();
            controller.setMessage("Do you want to delete " + selected.getName() + "?");

            Stage stage = new Stage();
            stage.setTitle("Confirm");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isConfirmed()) {
                ServiceController.deleteService(selected.getId());
                loadServices();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonNewOnAction(ActionEvent event) {
        openModal("/ServiceUpsertView.fxml", "New Service");
    }

    @FXML
    void buttonUpdateOnAction(ActionEvent event) {
        openModal("/ServiceUpsertView.fxml", "Update Service");
    }

    @FXML
    void selectButtonOnAction(ActionEvent event) {
        Service selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        AppState.getInstance().setSelectedService(selected);

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void openModal(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadServices();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clearFilterButtonOnAction(ActionEvent event) {
        nameInput.clear();
        minPriceInput.clear();
        maxPriceInput.clear();

        durationMinInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1440, 0));
        durationMaxInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1440, 60));
        maxParticipantsMinInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        maxParticipantsMaxInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));

        radioButtonBoth.setSelected(true);

        serviceTable.setItems(allServices);
    }

}
