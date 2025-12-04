package proyectoDesarrollo.controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoadingController {

    @FXML
    private StackPane root;

    public void initialize() {
        createLoadingAnimation();
    }

    private void createLoadingAnimation() {
        int circleCount = 8;
        double radius = 50;
        double circleRadius = 6;

        for (int i = 0; i < circleCount; i++) {
            Circle circle = new Circle(circleRadius, Color.web("#4676D7"));
            double angle = 2 * Math.PI / circleCount * i;
            circle.setTranslateX(radius * Math.cos(angle));
            circle.setTranslateY(radius * Math.sin(angle));
            root.getChildren().add(circle);

            FadeTransition ft = new FadeTransition(Duration.seconds(1), circle);
            ft.setFromValue(0.1);
            ft.setToValue(1.0);
            ft.setCycleCount(Animation.INDEFINITE);
            ft.setAutoReverse(true);
            ft.setDelay(Duration.seconds(i * 0.1));
            ft.play();
        }

        RotateTransition rt = new RotateTransition(Duration.seconds(2), root);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();
    }
}
