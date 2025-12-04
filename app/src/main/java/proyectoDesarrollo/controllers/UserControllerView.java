package proyectoDesarrollo.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyectoDesarrollo.interfaz.controllers.UserController;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.utils.AppState;

public class UserControllerView {

    public enum Mode {
        DEFAULT,
        SELECT_ONLY
    }

    private Mode mode = Mode.DEFAULT;

    // LISTA COMPLETA PARA FILTRAR
    private ObservableList<User> allUsers = FXCollections.observableArrayList();

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
    private TableColumn<User, String> addressColumn;
    @FXML
    private TextField addressInput;
    @FXML
    private Button buttonFilter;
    @FXML
    private Button buttonUpdate;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonNew;
    @FXML
    private Button clearFilterButton;
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
    @FXML
    private Button selectButton;

    public void initialize() {

        selectButton.setFocusTraversable(false);
        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        userTable.getSelectionModel().clearSelection();

        AppState appState = AppState.getInstance();
        appState.setSelectedUser(null);

        // Configuración de columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadUsers();

        // Menu contextual
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Update");
        editItem.setOnAction(e -> {
            User selected = userTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openModal("/UserUpsertView.fxml", "Actualizar Usuario");
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            User selected = userTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                confirmAndDeleteUser(selected);
            }
        });

        MenuItem exportItem = new MenuItem("Export to CSV");
        exportItem.setOnAction(e -> exportTableToCSV());

        contextMenu.getItems().addAll(editItem, deleteItem, exportItem);

        userTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            buttonUpdate.setDisable(!hasSelection);
            buttonDelete.setDisable(!hasSelection);

            AppState state = AppState.getInstance();
            state.setSelectedUser(newSelection);
        });

        userTable.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && mode != Mode.SELECT_ONLY) {
                if (!buttonUpdate.isFocused() && !buttonDelete.isFocused()) {
                    userTable.getSelectionModel().clearSelection();
                }
            }
        });

        setupMode();
    }

    private void loadUsers() {
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
                    ObservableList<User> users = UserController.getAllUsers();
                    allUsers = FXCollections.observableArrayList(users);

                    Platform.runLater(() -> {
                        userTable.setItems(allUsers);
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
        if (allUsers == null)
            return;

        String name = nameInput.getText().toLowerCase().trim();
        String email = emailInput.getText().toLowerCase().trim();
        String role = roleInput.getText().toLowerCase().trim();
        String phone = phoneInput.getText().toLowerCase().trim();
        String address = addressInput.getText().toLowerCase().trim();

        ObservableList<User> filtered = allUsers.filtered(user -> {
            boolean matchesName = name.isEmpty() || user.getUsername().toLowerCase().contains(name);
            boolean matchesEmail = email.isEmpty() || user.getEmail().toLowerCase().contains(email);
            boolean matchesRole = role.isEmpty() || user.getRole().toLowerCase().contains(role);
            boolean matchesPhone = phone.isEmpty() || user.getPhone().toLowerCase().contains(phone);
            boolean matchesAddress = address.isEmpty() || user.getAddress().toLowerCase().contains(address);

            return matchesName && matchesEmail && matchesRole && matchesPhone && matchesAddress;
        });

        userTable.setItems(filtered);
    }

    @FXML
    void buttonDeleteOnAction(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            confirmAndDeleteUser(selectedUser);
        }
    }

    @FXML
    void buttonNewOnAction(ActionEvent event) {
        openModal("/UserUpsertView.fxml", "Nuevo Usuario");
    }

    @FXML
    void buttonUpdateOnAction(ActionEvent event) {
        openModal("/UserUpsertView.fxml", "Actualizar Usuario");
    }

    @FXML
    void selectButtonOnAction(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            System.out.println("No user selected");
            return;
        }

        AppState.getInstance().setSelectedUser(selectedUser);

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

            loadUsers();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmAndDeleteUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConfirmView.fxml"));
            Parent root = loader.load();
            ConfirmController controller = loader.getController();
            controller.setMessage("Do you want to delete " + user.getUsername() + "?");

            Stage stage = new Stage();
            stage.setTitle("Confirm");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isConfirmed()) {
                UserController.deleteUser(user.getId());
                loadUsers();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clearFilterButtonOnAction(ActionEvent event) {
        nameInput.clear();
        emailInput.clear();
        roleInput.clear();
        phoneInput.clear();
        addressInput.clear();

        if (allUsers != null) {
            userTable.setItems(allUsers);
        }
    }

    private void exportTableToCSV() {
    if (userTable.getItems().isEmpty()) {
        System.out.println("No hay datos para exportar");
        return;
    }

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Guardar CSV");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    File file = fileChooser.showSaveDialog(userTable.getScene().getWindow());

    if (file != null) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Cabecera
            writer.write("ID,Username,Email,Role,Phone,Address");
            writer.newLine();

            // Filas
            for (User user : userTable.getItems()) {
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getPhone(),
                        user.getAddress());
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
