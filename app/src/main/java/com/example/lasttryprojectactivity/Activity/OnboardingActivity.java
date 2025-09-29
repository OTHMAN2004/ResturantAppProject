package com.example.lasttryprojectactivity.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lasttryprojectactivity.Adapters.OnboardingAdapter;
import com.example.lasttryprojectactivity.databinding.ActivityOnboardingBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnboardingActivity extends AppCompatActivity {
    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPager();
        setupListeners();
    }

    private void setupViewPager() {
        OnboardingAdapter adapter = new OnboardingAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {

                }).attach();
    }

    private void setupListeners() {
        binding.nextButton.setOnClickListener(v -> {
            if (binding.viewPager.getCurrentItem() < 2) {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            } else {
                startActivity(new Intent(OnboardingActivity.this, AuthActivity.class));
                finish();
            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.nextButton.setText(position == 2 ? "Get Started" : "Next");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}