package com.example.lasttryprojectactivity.RoomDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.example.lasttryprojectactivity.Class.Converters;
import com.example.lasttryprojectactivity.Class.User;
import com.example.lasttryprojectactivity.Class.MenuItem;
import com.example.lasttryprojectactivity.Class.CartItem;
import com.example.lasttryprojectactivity.Class.Order;

@Database(
        entities = {User.class, MenuItem.class, CartItem.class, Order.class},
        version = 3,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract com.example.lasttryprojectactivity.Dao.UserDao userDao();
    public abstract com.example.lasttryprojectactivity.Dao.MenuItemDao menuItemDao();
    public abstract com.example.lasttryprojectactivity.Dao.CartDao cartDao();
    public abstract com.example.lasttryprojectactivity.Dao.OrderDao orderDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "restaurant_database")
                            .allowMainThreadQueries()

                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}