package proyectoDesarrollo.controllers;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import proyectoDesarrollo.interfaz.JasperConnectionService;

public class ReportPanelControllerView {

    @FXML
    private CheckBox checkboxEmbedded;

    @FXML
    private Button savePDF;

    @FXML
    private Button createReport;

    @FXML
    private WebView webView;

    private ReportContext context;

    public enum ReportContext {
        USERS,
        SERVICES,
        ORDERS
    }

    public void setContext(ReportContext context) {
        this.context = context;
    }

    private final Map<String, Object> parametros = new HashMap<>();

    @FXML
    void createReportOnAction() {

        if (context == null)
            return;

        try {
            generarReporte();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void savePDFOnAction(ActionEvent event) {

        if (context == null)
            return;

        try {
            JasperPrint print = buildReport();

            File dir = new File("informes");
            if (!dir.exists())
                dir.mkdirs();

            String pdfPath = "informes/reporte.pdf";
            JasperExportManager.exportReportToPdfFile(print, pdfPath);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informe generado");
            alert.setHeaderText(null);
            alert.setContentText("El PDF se ha guardado correctamente en:\n" + pdfPath);
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo generar el PDF");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void generarReporte() throws Exception {

        JasperPrint print = buildReport();

        File dir = new File("informes");
        if (!dir.exists())
            dir.mkdirs();

        String htmlPath = "informes/reporte.html";
        String pdfPath = "informes/reporte.pdf";

        JasperExportManager.exportReportToHtmlFile(print, htmlPath);
        JasperExportManager.exportReportToPdfFile(print, pdfPath);

        if (checkboxEmbedded.isSelected()) {
            webView.getEngine().load(new File(htmlPath).toURI().toString());
        } else {
            WebView newWebView = new WebView();
            newWebView.getEngine().load(new File(htmlPath).toURI().toString());

            StackPane root = new StackPane(newWebView);
            Scene scene = new Scene(root, 900, 600);

            Stage stage = new Stage();
            stage.setTitle("Informe");
            stage.initModality(Modality.NONE);
            stage.setScene(scene);
            stage.show();
        }
    }

    private JasperPrint buildReport() throws Exception {

        String ruta = switch (context) {
            case USERS -> "/reports/Users.jasper";
            case SERVICES -> "/reports/Services.jasper";
            case ORDERS -> "/reports/Orders.jasper";
        };

        InputStream reportStream = getClass().getResourceAsStream(ruta);

        if (reportStream == null) {
            throw new RuntimeException("No se encontró el .jasper en: " + ruta);
        }

        JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);

        Connection conn = JasperConnectionService.getConnection();

        InputStream img = getClass().getResourceAsStream("/images/icon.png");
        parametros.put("img_header", img);
        parametros.put("title", switch (context) {
            case USERS -> "Users Report";
            case SERVICES -> "Services Report";
            case ORDERS -> "Orders Report";
        });

        return JasperFillManager.fillReport(report, parametros, conn);
    }

}