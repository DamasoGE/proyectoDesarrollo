package proyectoDesarrollo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label labelSaludo;

    @FXML
    void handleBotonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText("Información");
        alert.setContentText("¡Has pulsado el botón!");
        alert.showAndWait();
    }

    @FXML
    void handleBotonClick2(ActionEvent event) {

    }

}
