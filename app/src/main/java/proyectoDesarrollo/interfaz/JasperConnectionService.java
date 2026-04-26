package proyectoDesarrollo.interfaz;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JasperConnectionService {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties props = new Properties();

            try (InputStream is = JasperConnectionService.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties")) {

                if (is == null) {
                    throw new RuntimeException("No se encontró config.properties en resources");
                }

                props.load(is);
            }

            String ip = props.getProperty("IP");
            String port = props.getProperty("PORT");
            String db = props.getProperty("BBDD");

            USER = props.getProperty("USER");
            PASSWORD = props.getProperty("PWD");

            URL = "jdbc:mysql://" + ip + ":" + port + "/" + db +
                    "?useSSL=false&serverTimezone=UTC";

        } catch (Exception e) {
            throw new RuntimeException("Error cargando configuración de BD", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}