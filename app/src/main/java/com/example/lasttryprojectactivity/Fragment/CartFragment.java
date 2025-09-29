package com.example.lasttryprojectactivity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lasttryprojectactivity.Adapters.CartAdapter;
import com.example.lasttryprojectactivity.Class.CartItem;
import com.example.lasttryprojectactivity.R;
import com.example.lasttryprojectactivity.RoomDatabase.AppViewModel;
import com.example.lasttryprojectactivity.Utilities.SessionManager;
import com.example.lasttryprojectactivity.databinding.FragmentCartBinding;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private AppViewModel viewModel;
    private CartAdapter cartAdapter;
    private SessionManager sessionManager;
    private List<CartItem> cartItems = new ArrayList<>();
    private double totalAmount = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setupViewModel();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
    }

    private void initViews() {
        sessionManager = new SessionManager(requireContext());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(cartItems, viewModel);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.cartRecyclerView.setAdapter(cartAdapter);
    }

    private void setupObservers() {
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            viewModel.getCartItemsByUserId(userId).observe(getViewLifecycleOwner(), items -> {
                if (items != null) {
                    cartItems.clear();
                    cartItems.addAll(items);
                    cartAdapter.notifyDataSetChanged();
                    calculateAndUpdateTotal();
                }
            });

            viewModel.getCartStatus().observe(getViewLifecycleOwner(), status -> {
                if (status != null && !status.isEmpty()) {
                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                }
            });

            viewModel.getOrderStatus().observe(getViewLifecycleOwner(), status -> {
                if (status != null && !status.isEmpty()) {
                    Toast.makeText(getContext(), status, Toast.LENGTH_LONG).show();
                    if (status.contains("Order placed successfully")) {

                        cartItems.clear();
                        cartAdapter.notifyDataSetChanged();
                        binding.totalAmountTextView.setText("Total: $0.00");


                        Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void setupClickListeners() {
        binding.placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void calculateAndUpdateTotal() {
        totalAmount = viewModel.calculateCartTotal(cartItems);
        binding.totalAmountTextView.setText(String.format("Total: $%.2f", totalAmount));
    }

    private void placeOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Confirm Order")
                .setMessage("Do you want to place this order for $" + String.format("%.2f", totalAmount) + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    viewModel.placeOrder(userId, cartItems, totalAmount);
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}