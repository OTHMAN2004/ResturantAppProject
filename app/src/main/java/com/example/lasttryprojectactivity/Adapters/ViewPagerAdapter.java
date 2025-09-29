package com.example.lasttryprojectactivity.Adapters;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.lasttryprojectactivity.Fragment.MenuCategoryFragment;

import org.jspecify.annotations.NonNull;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final String[] categories = {"Starters", "Main Course", "Desserts", "Drinks"};

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MenuCategoryFragment.newInstance(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public String getCategoryTitle(int position) {
        return categories[position];
    }
}

