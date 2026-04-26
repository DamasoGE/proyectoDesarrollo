package proyectoDesarrollo.interfaz;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseService {

    private static DatabaseService instance;
    private Connection connection;

    private String URL;
    private String USER;
    private String PASSWORD;

    private DatabaseService() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            loadConfig();

            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MySQL no encontrado", e);
        }
    }

    public static DatabaseService getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseService();
        } else if (instance.getConnection() == null || instance.getConnection().isClosed()) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void loadConfig() throws SQLException {
        try {
            Properties props = new Properties();

            try (InputStream is = DatabaseService.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties")) {

                if (is == null) {
                    throw new SQLException("No se encontró config.properties en resources");
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
            throw new SQLException("Error cargando configuración de BD", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}