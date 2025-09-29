package com.example.lasttryprojectactivity.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.lasttryprojectactivity.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.lasttryprojectactivity.Fragment.HomeFragment;
import com.example.lasttryprojectactivity.Fragment.CartFragment;
import com.example.lasttryprojectactivity.Fragment.OrdersFragment;
import com.example.lasttryprojectactivity.Fragment.ProfileFragment;
import com.example.lasttryprojectactivity.Utilities.SessionManager;
import com.example.lasttryprojectactivity.Utilities.LocaleHelper;
import com.example.lasttryprojectactivity.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;


    private static final String PREFS_NAME = "ThemePrefs";
    private static final String THEME_KEY = "current_theme";
    private static final String LANG_PREFS = "LangPrefs";
    private static final String LANG_KEY = "current_lang";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        applySavedTheme();
        applySavedLanguage();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;


        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        updateNavigationMenu();


        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            } else if (itemId == R.id.nav_orders) {
                selectedFragment = new OrdersFragment();
            } else if (itemId == R.id.nav_profile) {
                if (sessionManager.isLoggedIn()) {
                    selectedFragment = new ProfileFragment();
                } else {
                    startActivity(new Intent(MainActivity.this, AuthActivity.class));
                    return false;
                }
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                loadFragment(new HomeFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_home);

            } else if (itemId == R.id.nav_cart) {
                loadFragment(new CartFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_cart);

            } else if (itemId == R.id.nav_orders) {
                if (sessionManager.isLoggedIn()) {
                    loadFragment(new OrdersFragment());
                    bottomNavigationView.setSelectedItemId(R.id.nav_orders);
                } else {
                    startActivity(new Intent(MainActivity.this, AuthActivity.class));
                    return true;
                }

            } else if (itemId == R.id.nav_profile) {
                if (sessionManager.isLoggedIn()) {
                    loadFragment(new ProfileFragment());
                    bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                } else {
                    startActivity(new Intent(MainActivity.this, AuthActivity.class));
                    return true;
                }

            } else if (itemId == R.id.nav_theme) {
                showThemeDialog();
                return true;

            } else if (itemId == R.id.nav_language) {
                showLanguageDialog();
                return true;

            } else if (itemId == R.id.nav_auth) {
                if (sessionManager.isLoggedIn()) {
                    showLogoutConfirmation();
                } else {
                    startActivity(new Intent(MainActivity.this, AuthActivity.class));
                }
                return true;
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }


    private void applySavedLanguage() {
        SharedPreferences prefs = getSharedPreferences(LANG_PREFS, MODE_PRIVATE);
        String lang = prefs.getString(LANG_KEY, "en"); // default English
        LocaleHelper.setLocale(this, lang);
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "العربية"};
        String[] codes = {"en", "ar"};

        SharedPreferences prefs = getSharedPreferences(LANG_PREFS, MODE_PRIVATE);
        String currentLang = prefs.getString(LANG_KEY, "en");
        int checkedItem = currentLang.equals("ar") ? 1 : 0;

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_language))
                .setSingleChoiceItems(languages, checkedItem, (dialog, which) -> {
                    saveLanguagePreference(codes[which]);
                    LocaleHelper.setLocale(this, codes[which]);
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveLanguagePreference(String lang) {
        SharedPreferences prefs = getSharedPreferences(LANG_PREFS, MODE_PRIVATE);
        prefs.edit().putString(LANG_KEY, lang).apply();
    }


    private void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedTheme = prefs.getInt(THEME_KEY, 0); // 0 = System Default
        applyTheme(savedTheme);
    }

    private void applyTheme(int theme) {
        switch (theme) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

    private void showThemeDialog() {
        String[] themes = {"System Default", "Light", "Dark"};
        int currentTheme = getCurrentTheme();

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_theme))
                .setSingleChoiceItems(themes, currentTheme, (dialog, which) -> {
                    saveThemePreference(which);
                    applyTheme(which);
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int getCurrentTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt(THEME_KEY, 0);
    }

    private void saveThemePreference(int theme) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(THEME_KEY, theme);
        editor.apply();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void updateNavigationMenu() {
        NavigationView navigationView = binding.navView;
        android.view.Menu menu = navigationView.getMenu();
        android.view.MenuItem authItem = menu.findItem(R.id.nav_auth);

        if (sessionManager.isLoggedIn()) {
            authItem.setTitle(getString(R.string.logout));
            authItem.setIcon(android.R.drawable.ic_lock_power_off);
        } else {
            authItem.setTitle(getString(R.string.login));
            authItem.setIcon(android.R.drawable.ic_dialog_alert);
        }
    }

    private void showLogoutConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_confirm))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> logout())
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void logout() {
        sessionManager.logoutUser();
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(androidx.core.view.GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationMenu();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
