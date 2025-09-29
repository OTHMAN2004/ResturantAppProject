package com.example.lasttryprojectactivity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lasttryprojectactivity.Class.MenuItem;
import com.example.lasttryprojectactivity.R;
import com.example.lasttryprojectactivity.RoomDatabase.AppDatabase;
import com.example.lasttryprojectactivity.Utilities.SessionManager;
import com.example.lasttryprojectactivity.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    private ActivitySplashBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);


        initializeSampleData();

        new Handler().postDelayed(() -> {
            Intent intent;
            if (sessionManager.isFirstLaunch()) {
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
                sessionManager.setFirstLaunch(false);
            } else if (sessionManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, AuthActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }

    private void initializeSampleData() {
        AppDatabase database = AppDatabase.getDatabase(this);


        new Thread(() -> {
            try {

                insertSampleDataIfEmpty(database);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void insertSampleDataIfEmpty(AppDatabase database) {
        try {

            database.menuItemDao().insert(new MenuItem("Caesar Salad", "Fresh romaine lettuce with Caesar dressing", 8.99, "Starters", R.drawable.salad_dish));
            database.menuItemDao().insert(new MenuItem("Garlic Bread", "Toasted bread with garlic butter", 5.99, "Starters", R.drawable.garlic_bread));
            database.menuItemDao().insert(new MenuItem("Grilled Salmon", "Fresh salmon with herbs and lemon", 18.99, "Main Course", R.drawable.salmon_dish));
            database.menuItemDao().insert(new MenuItem("Chicken Alfredo", "Pasta with creamy Alfredo sauce", 15.99, "Main Course", R.drawable.pasta));
            database.menuItemDao().insert(new MenuItem("Chocolate Cake", "Rich chocolate cake with frosting", 6.99, "Desserts", R.drawable.cake));
            database.menuItemDao().insert(new MenuItem("Ice Cream", "Vanilla ice cream with toppings", 4.99, "Desserts", R.drawable.ice_ream));
            database.menuItemDao().insert(new MenuItem("Coca Cola", "Classic cola drink", 2.99, "Drinks", R.drawable.coca));
            database.menuItemDao().insert(new MenuItem("Orange Juice", "Fresh squeezed orange juice", 3.99, "Drinks", R.drawable.drink_dish));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}