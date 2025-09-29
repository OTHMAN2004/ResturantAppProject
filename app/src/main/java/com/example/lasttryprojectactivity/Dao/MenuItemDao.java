package com.example.lasttryprojectactivity.Dao;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.lasttryprojectactivity.Class.MenuItem;

import java.util.List;


@Dao
public interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    LiveData<List<MenuItem>> getAllMenuItems();

    @Query("SELECT * FROM menu_items WHERE category = :category")
    LiveData<List<MenuItem>> getMenuItemsByCategory(String category);

    @Insert
    long insert(MenuItem menuItem);

    @Update
    void update(MenuItem menuItem);

    @Delete
    void delete(MenuItem menuItem);

    @Query("SELECT * FROM menu_items WHERE id = :id")
    LiveData<MenuItem> getMenuItemById(int id);

    @Query("SELECT COUNT(*) FROM menu_items")
    int getMenuItemsCount();

}
