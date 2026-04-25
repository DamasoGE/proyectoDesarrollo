package proyectoDesarrollo.interfaz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JasperConnectionService {

    private static final String URL =
            "jdbc:mysql://localhost:3306/eventik?useSSL=false&serverTimezone=UTC";
    private static final String USER = "admin";
    private static final String PASSWORD = "5Y0LO6VwWAaG";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}