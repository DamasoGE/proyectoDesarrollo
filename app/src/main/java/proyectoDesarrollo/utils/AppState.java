package proyectoDesarrollo.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AppState {
    private static AppState instance;

    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();

    private AppState() {}

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    // Getters de las propiedades
    public BooleanProperty loggedInProperty() { return loggedIn; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty roleProperty() { return role; }

    // Métodos convenientes
    public boolean isLoggedIn() { return loggedIn.get(); }
    public void setLoggedIn(boolean value) { loggedIn.set(value); }

    public String getUsername() { return username.get(); }
    public void setUsername(String value) { username.set(value); }

    public String getRole() { return role.get(); }
    public void setRole(String value) { role.set(value); }

    public void logout() {
        loggedIn.set(false);
        username.set(null);
        role.set(null);
    }
}