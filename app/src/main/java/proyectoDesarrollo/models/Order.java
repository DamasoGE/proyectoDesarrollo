package proyectoDesarrollo.models;

import java.time.LocalDateTime;

public class Order {
    private String id;
    private String customerId;
    private String serviceId;
    private LocalDateTime appointment;
    private String status;
    private String notes;
    private double priceFinal;
    private int participants;
    private String location;

    public Order(String customerId, String serviceId, LocalDateTime appointment, String status, String notes,
            double priceFinal, int participants, String location) {
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.appointment = appointment;
        this.status = status;
        this.notes = notes;
        this.priceFinal = priceFinal;
        this.participants = participants;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String id) {
        this.customerId = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String id) {
        this.serviceId = id;
    }

    public LocalDateTime getAppointment() {
        return appointment;
    }

    public void setAppointment(LocalDateTime appointment) {
        this.appointment = appointment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getPriceFinal() {
        return priceFinal;
    }

    public void setPriceFinal(double priceFinal) {
        this.priceFinal = priceFinal;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    

    
}
