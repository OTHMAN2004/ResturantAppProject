package com.example.lasttryprojectactivity.RoomDatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.lasttryprojectactivity.Class.CartItem;
import com.example.lasttryprojectactivity.Class.MenuItem;
import com.example.lasttryprojectactivity.Class.Order;
import com.example.lasttryprojectactivity.Class.User;
import com.example.lasttryprojectactivity.Dao.CartDao;
import com.example.lasttryprojectactivity.Dao.MenuItemDao;
import com.example.lasttryprojectactivity.Dao.OrderDao;
import com.example.lasttryprojectactivity.Dao.UserDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private UserDao userDao;
    private MenuItemDao menuItemDao;
    private CartDao cartDao;
    private OrderDao orderDao;
    private ExecutorService executor;

    public Repository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        userDao = database.userDao();
        menuItemDao = database.menuItemDao();
        cartDao = database.cartDao();
        orderDao = database.orderDao();
        executor = Executors.newFixedThreadPool(4);
    }


    public void insertUser(User user) {
        executor.execute(() -> userDao.insert(user));
    }

    public User login(String email, String password) {
        return userDao.login(email, password);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public LiveData<User> getUserById(int id) {
        return userDao.getUserById(id);
    }

    public void updateUser(User user) {
        executor.execute(() -> userDao.update(user));
    }


    public LiveData<List<MenuItem>> getMenuItemsByCategory(String category) {
        return menuItemDao.getMenuItemsByCategory(category);
    }

    public LiveData<List<MenuItem>> getAllMenuItems() {
        return menuItemDao.getAllMenuItems();
    }

    public LiveData<MenuItem> getMenuItemById(int id) {
        return menuItemDao.getMenuItemById(id);
    }

    public void insertMenuItem(MenuItem menuItem) {
        executor.execute(() -> menuItemDao.insert(menuItem));
    }


    public LiveData<List<CartItem>> getCartItemsByUserId(int userId) {
        return cartDao.getCartItemsByUserId(userId);
    }

    public void insertCartItem(CartItem cartItem) {
        executor.execute(() -> cartDao.insert(cartItem));
    }

    public void updateCartItem(CartItem cartItem) {
        executor.execute(() -> cartDao.update(cartItem));
    }

    public void deleteCartItem(CartItem cartItem) {
        executor.execute(() -> cartDao.delete(cartItem));
    }

    public void clearCart(int userId) {
        executor.execute(() -> cartDao.clearCart(userId));
    }

    public LiveData<Integer> getCartItemCount(int userId) {
        return cartDao.getCartItemCount(userId);
    }


    public LiveData<List<Order>> getOrdersByUserId(int userId) {
        return orderDao.getOrdersByUserId(userId);
    }

    public void insertOrder(Order order) {
        executor.execute(() -> orderDao.insert(order));
    }

    public void updateOrder(Order order) {
        executor.execute(() -> orderDao.update(order));
    }

    public LiveData<Order> getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }
}