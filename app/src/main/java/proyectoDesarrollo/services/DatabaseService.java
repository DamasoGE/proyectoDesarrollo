package proyectoDesarrollo.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectoDesarrollo.models.Service;
import proyectoDesarrollo.models.User;
import proyectoDesarrollo.models.Order;

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

    public User login(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("address"));
                user.setId(rs.getString("id"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM user";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("address"));
                user.setId(rs.getString("id"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public ObservableList<Service> getAllServices() {
        ObservableList<Service> services = FXCollections.observableArrayList();
        String sql = "SELECT * FROM service";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Service service = new Service(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("duration"),
                        rs.getInt("maxParticipants"),
                        rs.getBoolean("isActive"));
                service.setId(rs.getString("id"));
                services.add(service);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    public ObservableList<Order> getAllOrders() {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String sql = "SELECT * FROM `order`";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("appointment");
                LocalDateTime appointment = timestamp != null ? timestamp.toLocalDateTime() : null;
                Order order = new Order(
                        rs.getString("customerId"),
                        rs.getString("serviceId"),
                        appointment,
                        rs.getString("status"),
                        rs.getString("notes"),
                        rs.getDouble("priceFinal"),
                        rs.getInt("participants"),
                        rs.getString("location"));
                order.setId(rs.getString("id"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}
