package proyectoDesarrollo.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {

    private static DatabaseService instance;
    private Connection connection;

    // Configuración de la conexión
    private final String URL = "jdbc:mysql://localhost:3306/eventik?useSSL=false&serverTimezone=UTC";
    private final String USER = "admin";
    private final String PASSWORD = "1NnI6SeaDHDG";

    // Constructor privado (singleton)
    private DatabaseService() throws SQLException {
        try {
            // Cargar driver (opcional con MySQL moderno)
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL exitosa");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MySQL no encontrado", e);
        }
    }

    // Obtener instancia singleton
    public static DatabaseService getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseService();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // Obtener conexión
    public Connection getConnection() {
        return connection;
    }

    // Cerrar conexión
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
