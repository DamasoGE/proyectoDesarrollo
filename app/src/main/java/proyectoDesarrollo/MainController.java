package proyectoDesarrollo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class MainController {

    @FXML
    private VBox sidebarContainer;

    @FXML
    private StackPane contentContainer;

    @FXML
    public void initialize() {
        loadSidebar();
        loadLoginView();
    }

    /**
     * Carga la vista del login en el centro
     */
    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent loginView = loader.load(); // Parent funciona para cualquier nodo raíz
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(loginView);
            contentContainer.setAlignment(Pos.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga el contenido del sidebar
     */
    private void loadSidebar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SidebarGuestView.fxml"));
            Parent sidebar = loader.load();
            sidebarContainer.getChildren().add(sidebar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
