package proyectoDesarrollo.interfaz.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectoDesarrollo.interfaz.DatabaseService;
import proyectoDesarrollo.models.Order;

public class OrderController {

    public static ObservableList<Order> getAllOrders() {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        String sql = """
                SELECT
                    o.*,
                    u.username AS customerName,
                    s.name AS serviceName
                FROM orders o
                JOIN users u ON o.customerId = u.id
                JOIN services s ON o.serviceId = s.id
                    """;

        try (Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Order order = new Order(
                        rs.getString("customerId"),
                        rs.getString("serviceId"),
                        rs.getTimestamp("appointment"),
                        rs.getString("status"),
                        rs.getString("notes"),
                        rs.getDouble("priceFinal"),
                        rs.getInt("participants"),
                        rs.getString("location"));

                // ID interno
                order.setId(rs.getString("id"));

                // Datos extra para la vista
                order.setCustomerName(rs.getString("customerName"));
                order.setServiceName(rs.getString("serviceName"));

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    public static boolean addOrder(Order order) {
        String sql = "INSERT INTO orders (customerId, serviceId, appointment, status, notes, priceFinal, participants, location, id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (order.getId() == null || order.getId().isBlank()) {
                order.setId(java.util.UUID.randomUUID().toString());
            }

            stmt.setString(1, order.getCustomerId());
            stmt.setString(2, order.getServiceId());
            stmt.setTimestamp(3, order.getAppointment());
            stmt.setString(4, order.getStatus());
            stmt.setString(5, order.getNotes());
            stmt.setDouble(6, order.getPriceFinal());
            stmt.setInt(7, order.getParticipants());
            stmt.setString(8, order.getLocation());
            stmt.setString(9, order.getId());         

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET customerId=?, serviceId=?, appointment=?, status=?, notes=?, priceFinal=?, participants=?, location=? "
                   + "WHERE id=?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, order.getCustomerId());
            stmt.setString(2, order.getServiceId());
            stmt.setTimestamp(3, order.getAppointment());
            stmt.setString(4, order.getStatus());
            stmt.setString(5, order.getNotes());
            stmt.setDouble(6, order.getPriceFinal());
            stmt.setInt(7, order.getParticipants());
            stmt.setString(8, order.getLocation());
            stmt.setString(9, order.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteOrder(String orderId) {
        String sql = "DELETE FROM orders WHERE id=?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, orderId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static Order getOrderById(String orderId) {
        String sql = "SELECT * FROM orders WHERE id=?";
        Order order = null;

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order(
                            rs.getString("customerId"),
                            rs.getString("serviceId"),
                            rs.getTimestamp("appointment"),
                            rs.getString("status"),
                            rs.getString("notes"),
                            rs.getDouble("priceFinal"),
                            rs.getInt("participants"),
                            rs.getString("location")
                    );
                    order.setId(rs.getString("id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

}
