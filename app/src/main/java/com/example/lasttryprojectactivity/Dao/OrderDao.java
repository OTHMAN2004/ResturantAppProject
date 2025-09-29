package com.example.lasttryprojectactivity.Dao;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lasttryprojectactivity.Class.Order;

import java.util.List;


@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    LiveData<List<Order>> getOrdersByUserId(int userId);

    @Query("SELECT * FROM orders WHERE id = :id")
    LiveData<Order> getOrderById(int id);
}