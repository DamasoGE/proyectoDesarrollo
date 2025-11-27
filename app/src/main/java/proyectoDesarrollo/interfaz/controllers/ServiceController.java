package proyectoDesarrollo.interfaz.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectoDesarrollo.interfaz.DatabaseService;
import proyectoDesarrollo.models.Service;


public class ServiceController {

        public static boolean addService(Service service) {
        String sql = "INSERT INTO services (id, name, description, price, duration, maxParticipants, isActive) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {


            if (service.getId() == null || service.getId().isBlank()) {
                service.setId(java.util.UUID.randomUUID().toString());
            }

            stmt.setString(1, service.getId());
            stmt.setString(2, service.getName());
            stmt.setString(3, service.getDescription());
            stmt.setDouble(4, service.getPrice());
            stmt.setInt(5, service.getDuration());
            stmt.setInt(6, service.getMaxParticipants());
            stmt.setBoolean(7, service.isActive());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ObservableList<Service> getAllServices() {
        ObservableList<Service> services = FXCollections.observableArrayList();
        String sql = "SELECT * FROM services";

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }


    public static boolean updateService(Service service) {
        String sql = "UPDATE services SET name=?, description=?, price=?, duration=?, maxParticipants=?, isActive=? "
                   + "WHERE id=?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, service.getName());
            stmt.setString(2, service.getDescription());
            stmt.setDouble(3, service.getPrice());
            stmt.setInt(4, service.getDuration());
            stmt.setInt(5, service.getMaxParticipants());
            stmt.setBoolean(6, service.isActive());
            stmt.setString(7, service.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteService(String serviceId) {
        String sql = "DELETE FROM services WHERE id=?";

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, serviceId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Service getServiceById(String serviceId) {
        String sql = "SELECT * FROM services WHERE id=?";
        Service service = null;

        try (Connection connection = DatabaseService.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, serviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    service = new Service(
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("duration"),
                            rs.getInt("maxParticipants"),
                            rs.getBoolean("isActive"));
                    service.setId(rs.getString("id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return service;
    }
}
