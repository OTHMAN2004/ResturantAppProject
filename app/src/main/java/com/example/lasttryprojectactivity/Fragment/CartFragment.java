package com.example.lasttryprojectactivity.Fragment;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lasttryprojectactivity.Activity.MainActivity;
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

    private static final String CHANNEL_ID = "order_channel";

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
//        createNotificationChannel();
        setupViewModel();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
        requestNotificationPermissionIfNeeded();
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

                        showOrderNotification(totalAmount);

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

    //    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelName = "Order Notifications";
//            String channelDesc = "Notifications for placed orders";
//
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    channelName,
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            channel.setDescription(channelDesc);
//
//            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
//            if (manager != null) {
//                manager.createNotificationChannel(channel);
//            }
//        }
//    }
    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void showOrderNotification(double amount) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_shopping_cart)
                .setContentTitle("تم الطلب بنجاح ")
                .setContentText("طلبك بقيمة $" + String.format("%.2f", amount) + " قيد المعالجة")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
