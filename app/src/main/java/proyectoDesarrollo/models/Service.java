package proyectoDesarrollo.models;

public class Service {
    private String id;
    private String name;
    private String description;
    private double price;
    private int duration;
    private int maxParticipants;
    private boolean isActive;

    public Service(String name, String description, double price, int duration, int maxParticipants,
            boolean isActive) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.maxParticipants = maxParticipants;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }



}
