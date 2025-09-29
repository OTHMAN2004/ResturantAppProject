package com.example.lasttryprojectactivity.Dao;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.lasttryprojectactivity.Class.CartItem;

import java.util.List;

@Dao
public interface CartDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    LiveData<List<CartItem>> getCartItemsByUserId(int userId);

    @Insert
    long insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    void clearCart(int userId);

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    LiveData<Integer> getCartItemCount(int userId);
}
