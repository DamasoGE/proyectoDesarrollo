package proyectoDesarrollo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import proyectoDesarrollo.controllers.SidebarLoggedController;
import proyectoDesarrollo.utils.AppState;

import java.io.IOException;

public class MainController {

    @FXML
    private VBox sidebarContainer;

    @FXML
    private StackPane contentContainer;

    @FXML
    public void initialize() {
        AppState state = AppState.getInstance();

        // Escucha cambios de login
        state.loggedInProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                loadSidebarLogged();
                loadHomeView();
            } else {
                loadSidebarGuest();
                loadLoginView();
            }
        });

        // Carga inicial
        if (state.isLoggedIn()) {
            loadSidebarLogged();
            loadHomeView();
        } else {
            loadSidebarGuest();
            loadLoginView();
        }
    }

    /** Carga la vista del login en el centro */
    private void loadLoginView() {
        loadView("/LoginView.fxml");
    }

    /** Carga la vista principal (home) en el centro */
    private void loadHomeView() {
        loadView("/HomeView.fxml");
    }

    /** Carga el contenido del sidebar invitado */
    private void loadSidebarGuest() {
        loadSidebar("/SidebarGuestView.fxml");
    }

    /** Carga el contenido del sidebar cuando el usuario está logueado */
    private void loadSidebarLogged() {
        loadSidebar("/SidebarLoggedView.fxml");
    }

    /** Utilidad para cargar el contenido principal */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentContainer.getChildren().setAll(view);
            contentContainer.setAlignment(Pos.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Utilidad para cargar el sidebar */
    private void loadSidebar(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sidebar = loader.load();

            // Obtener el controlador y pasarle la referencia
            Object controller = loader.getController();
            if (controller instanceof SidebarLoggedController) {
                ((SidebarLoggedController) controller).setMainController(this);
            }

            sidebarContainer.getChildren().setAll(sidebar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRightContent(Node node) {
        contentContainer.getChildren().setAll(node);
    }
}
