package com.example.lasttryprojectactivity.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lasttryprojectactivity.Adapters.MenuItemAdapter;
import com.example.lasttryprojectactivity.Class.CartItem;
import com.example.lasttryprojectactivity.Class.MenuItem;
import com.example.lasttryprojectactivity.R;
import com.example.lasttryprojectactivity.RoomDatabase.AppViewModel;
import com.example.lasttryprojectactivity.Utilities.SessionManager;
import com.example.lasttryprojectactivity.databinding.FragmentMenuCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class MenuCategoryFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";
    private String category;
    private FragmentMenuCategoryBinding binding;
    private MenuItemAdapter adapter;
    private AppViewModel viewModel;
    private SessionManager sessionManager;

    private List<MenuItem> originalMenuItems = new ArrayList<>();

    public static MenuCategoryFragment newInstance(String category) {
        MenuCategoryFragment fragment = new MenuCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        loadMenuItems();
    }

    private void setupRecyclerView() {
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MenuItemAdapter(getContext(), null);
        binding.menuRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem menuItem) {
                Toast.makeText(getContext(), menuItem.getName() + " clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddToCartClick(MenuItem menuItem) {
                int userId = sessionManager.getUserId();
                if (userId != -1) {
                    CartItem cartItem = new CartItem(
                            userId,
                            menuItem.getId(),
                            menuItem.getName(),
                            menuItem.getPrice(),
                            1,
                            menuItem.getImageResourceId()
                    );
                    viewModel.addToCart(cartItem);
                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please login to add items to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMenuItems() {
        viewModel.getMenuItemsByCategory(category).observe(getViewLifecycleOwner(), menuItems -> {
            if (menuItems != null) {
                adapter.updateMenuItems(menuItems);
                originalMenuItems.clear();
                originalMenuItems.addAll(menuItems);
            }
        });
    }

    public void filterMenuItems(String query) {
        if (adapter == null) return;

        if (query == null || query.trim().isEmpty()) {
            adapter.updateMenuItems(originalMenuItems);
            return;
        }

        List<MenuItem> filteredList = new ArrayList<>();
        String searchQuery = query.toLowerCase().trim();

        for (MenuItem item : originalMenuItems) {
            if (item != null && (
                    (item.getName() != null && item.getName().toLowerCase().contains(searchQuery)) ||
                            (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchQuery))
            )) {
                filteredList.add(item);
            }
        }

        adapter.updateMenuItems(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No items found for: " + query, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
