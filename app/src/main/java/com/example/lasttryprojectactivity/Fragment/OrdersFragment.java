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
import java.util.ArrayList;
import java.util.List;
import com.example.lasttryprojectactivity.Adapters.OrderAdapter;
import com.example.lasttryprojectactivity.Class.Order;
import com.example.lasttryprojectactivity.RoomDatabase.AppViewModel;
import com.example.lasttryprojectactivity.Utilities.SessionManager;
import com.example.lasttryprojectactivity.databinding.FragmentOrdersBinding;

public class OrdersFragment extends Fragment {
    private FragmentOrdersBinding binding;
    private AppViewModel viewModel;
    private OrderAdapter orderAdapter;
    private SessionManager sessionManager;
    private List<Order> ordersList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupRecyclerView();
        loadOrders();
        observeViewModel();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        sessionManager = new SessionManager(requireContext());
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(ordersList);
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ordersRecyclerView.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            viewModel.getOrdersByUserId(userId).observe(getViewLifecycleOwner(), orders -> {
                if (orders != null) {
                    ordersList.clear();
                    ordersList.addAll(orders);
                    orderAdapter.notifyDataSetChanged();


                    if (orders.isEmpty()) {
                        binding.emptyOrdersTextView.setVisibility(View.VISIBLE);
                        binding.emptyOrdersTextView.setText("No orders yet. Place your first order!");
                    } else {
                        binding.emptyOrdersTextView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void observeViewModel() {

        viewModel.getOrderStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null && status.contains("Order placed successfully")) {

                loadOrders();
                Toast.makeText(getContext(), status, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadOrders();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}