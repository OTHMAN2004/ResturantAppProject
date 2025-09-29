package com.example.lasttryprojectactivity.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.lasttryprojectactivity.Fragment.LoginFragment;
import com.example.lasttryprojectactivity.Fragment.RegisterFragment;
import com.example.lasttryprojectactivity.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPager();

    }

    private void setupViewPager() {
        AuthPagerAdapter adapter = new AuthPagerAdapter(this);
        viewPager = binding.viewPager;
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Login" : "Register")
        ).attach();
    }


    public void switchToLoginTab() {
        if (viewPager != null) {
            viewPager.post(() -> viewPager.setCurrentItem(0));
        }
    }

    private class AuthPagerAdapter extends FragmentStateAdapter {
        public AuthPagerAdapter(AuthActivity activity) {
            super(activity);
        }

        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? new LoginFragment() : new RegisterFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}