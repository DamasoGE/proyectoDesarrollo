package proyectoDesarrollo.models;

import java.sql.Date;

public class Order {
    private String id;
    private String customerId;
    private String serviceId;
    private Date appointment;
    private String status;
    private String notes;
    private Double priceFinal;
    private Integer participants;
    private String location;

    private String customerName;
    private String serviceName;

    public Order() {
    }

    public Order(String customerId, String serviceId, Date appointment, String status, String notes,
            Double priceFinal, int participants, String location) {
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

    public Date getAppointment() {
        return appointment;
    }

    public void setAppointment(Date appointment) {
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

    public Double getPriceFinal() {
        return priceFinal;
    }

    public void setPriceFinal(Double priceFinal) {
        this.priceFinal = priceFinal;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
