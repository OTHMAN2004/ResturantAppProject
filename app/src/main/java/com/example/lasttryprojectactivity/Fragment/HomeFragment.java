package com.example.lasttryprojectactivity.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import com.example.lasttryprojectactivity.Adapters.ViewPagerAdapter;
import com.example.lasttryprojectactivity.R;
import com.example.lasttryprojectactivity.Utilities.SessionManager;
import com.example.lasttryprojectactivity.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SessionManager sessionManager;
    private ViewPagerAdapter pagerAdapter;
    private String[] categories = {"Starters", "Main Course", "Desserts", "Drinks"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setupViewPagerAndTabs();
        setupSearchView();
    }

    private void initViews() {
        sessionManager = new SessionManager(requireContext());
    }

    private void setupViewPagerAndTabs() {
        pagerAdapter = new ViewPagerAdapter(this);
        binding.categoryViewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.categoryTabLayout, binding.categoryViewPager,
                (tab, position) -> tab.setText(categories[position])
        ).attach();
    }

    private void setupSearchView() {
        if (binding.searchView != null) {
            binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterCurrentTab(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterCurrentTab(newText);
                    return false;
                }
            });
        }
    }

    private void filterCurrentTab(String query) {
        int currentPos = binding.categoryViewPager.getCurrentItem();
        Fragment currentFragment = getChildFragmentManager().findFragmentByTag("f" + currentPos);

        if (currentFragment instanceof MenuCategoryFragment) {
            ((MenuCategoryFragment) currentFragment).filterMenuItems(query);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
