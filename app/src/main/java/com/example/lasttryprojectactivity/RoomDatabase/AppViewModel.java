package com.example.lasttryprojectactivity.RoomDatabase;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lasttryprojectactivity.Class.User;
import com.example.lasttryprojectactivity.Class.MenuItem;
import com.example.lasttryprojectactivity.Class.CartItem;
import com.example.lasttryprojectactivity.Class.Order;
import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private Repository repository;
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<String> cartStatus = new MutableLiveData<>();
    private MutableLiveData<String> orderStatus = new MutableLiveData<>();

    public AppViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    // User ViewModel Methods
    public void registerUser(User user) {
        repository.insertUser(user);
        // استخدام.postValue() بدلاً من.setValue() للعمل في الخلفية
        cartStatus.postValue("User registered successfully");
    }

    public User loginUser(String email, String password) {
        User user = repository.login(email, password);
        if (user != null) {
            currentUser.postValue(user); // استخدام.postValue()
        }
        return user;
    }

    public LiveData<User> getUserById(int userId) {
        return repository.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public void updateUser(User user) {
        repository.updateUser(user);
        currentUser.postValue(user); // استخدام.postValue()
        cartStatus.postValue("Profile updated successfully");
    }

    public User getCurrentUser() {
        return currentUser.getValue();
    }

    public void setCurrentUser(User user) {
        currentUser.postValue(user); // استخدام.postValue()
    }

    // MenuItem ViewModel Methods
    public LiveData<List<MenuItem>> getMenuItemsByCategory(String category) {
        return repository.getMenuItemsByCategory(category);
    }

    public LiveData<List<MenuItem>> getAllMenuItems() {
        return repository.getAllMenuItems();
    }

    public LiveData<MenuItem> getMenuItemById(int id) {
        return repository.getMenuItemById(id);
    }

    // Cart ViewModel Methods
    public LiveData<List<CartItem>> getCartItemsByUserId(int userId) {
        return repository.getCartItemsByUserId(userId);
    }

    public void addToCart(CartItem cartItem) {
        repository.insertCartItem(cartItem);
        cartStatus.postValue("Item added to cart"); // استخدام.postValue()
    }

    public void updateCartItem(CartItem cartItem) {
        repository.updateCartItem(cartItem);
        cartStatus.postValue("Cart updated"); // استخدام.postValue()
    }

    public void removeFromCart(CartItem cartItem) {
        repository.deleteCartItem(cartItem);
        cartStatus.postValue("Item removed from cart"); // استخدام.postValue()
    }

    public void clearCart(int userId) {
        repository.clearCart(userId);
        cartStatus.postValue("Cart cleared"); // استخدام.postValue()
    }

    public LiveData<Integer> getCartItemCount(int userId) {
        return repository.getCartItemCount(userId);
    }

    public LiveData<String> getCartStatus() {
        return cartStatus;
    }

    // Order ViewModel Methods
    public LiveData<List<Order>> getOrdersByUserId(int userId) {
        return repository.getOrdersByUserId(userId);
    }

    public void placeOrder(Order order) {
        repository.insertOrder(order);
        orderStatus.postValue("Order placed successfully"); // استخدام.postValue()
    }

    public void updateOrder(Order order) {
        repository.updateOrder(order);
        orderStatus.postValue("Order updated"); // استخدام.postValue()
    }

    public LiveData<Order> getOrderById(int orderId) {
        return repository.getOrderById(orderId);
    }

    public LiveData<String> getOrderStatus() {
        return orderStatus;
    }

    // Place Order Method
    public void placeOrder(int userId, List<CartItem> cartItems, double totalAmount) {
        // Convert cart items to JSON string for order items
        StringBuilder itemsJson = new StringBuilder();
        itemsJson.append("[");
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            itemsJson.append("{")
                    .append("\"name\":\"").append(item.getMenuItemName()).append("\",")
                    .append("\"quantity\":").append(item.getQuantity()).append(",")
                    .append("\"price\":").append(item.getPrice())
                    .append("}");
            if (i < cartItems.size() - 1) {
                itemsJson.append(",");
            }
        }
        itemsJson.append("]");

        // Create order
        Order order = new Order(userId, System.currentTimeMillis(), totalAmount, "PENDING", itemsJson.toString());
        repository.insertOrder(order);

        // Clear cart after placing order
        repository.clearCart(userId);

        orderStatus.postValue("Order placed successfully! Your order ID: " + order.getId());
    }

    // Calculate total cart amount
    public double calculateCartTotal(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}