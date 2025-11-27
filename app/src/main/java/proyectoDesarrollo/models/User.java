package proyectoDesarrollo.models;

public class User {
    private String id;
    private String username;
    private String email;
    private String role;
    private String phone;
    private String address;
    private String image;
    private String password;

    public User(String username, String email, String role, String phone, String address, String image) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
