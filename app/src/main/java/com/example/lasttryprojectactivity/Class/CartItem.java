package com.example.lasttryprojectactivity.Class;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int menuItemId;
    private String menuItemName;
    private double price;
    private int quantity;
    private int imageResourceId;

    public CartItem() {}


    public CartItem(int userId, int menuItemId, String menuItemName, double price, int quantity) {
        this.userId = userId;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.price = price;
        this.quantity = quantity;
        this.imageResourceId = 0;
    }


    public CartItem(int userId, int menuItemId, String menuItemName, double price, int quantity, int imageResourceId) {
        this.userId = userId;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.price = price;
        this.quantity = quantity;
        this.imageResourceId = imageResourceId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMenuItemId() { return menuItemId; }
    public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }

    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getImageResourceId() { return imageResourceId; }
    public void setImageResourceId(int imageResourceId) { this.imageResourceId = imageResourceId; }

    public double getTotalPrice() {
        return price * quantity;
    }
}