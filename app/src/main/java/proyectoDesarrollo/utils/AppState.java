package proyectoDesarrollo.utils;

import proyectoDesarrollo.models.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import proyectoDesarrollo.models.Order;
import proyectoDesarrollo.models.Service;

public final class AppState {

    private static AppState instance;

    // ==============================
    // SESIÓN
    // ==============================
    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

    // ==============================
    // SELECCIONES GLOBALES
    // ==============================
    private Order selectedOrder;
    private Service selectedService;
    private User selectedUser;

    private AppState() {
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    // ==============================
    // --- SESSION ---
    // ==============================

    public boolean isLoggedIn() {
        return loggedIn.get();
    }

    public void setLoggedIn(boolean value) {
        loggedIn.set(value);
    }

    public BooleanProperty loggedInProperty() {
        return loggedIn;
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    // ==============================
    // --- SELECTED ---
    // ==============================

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order order) {
        this.selectedOrder = order;
    }

    public Service getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(Service service) {
        this.selectedService = service;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User user) {
        this.selectedUser = user;
    }

    // ==============================
    // LIMPIAR ESTADO (LOGOUT)
    // ==============================

    public void clear() {
        loggedIn.set(false);
        currentUser.set(null);
        selectedOrder = null;
        selectedService = null;
        selectedUser = null;
    }
}
