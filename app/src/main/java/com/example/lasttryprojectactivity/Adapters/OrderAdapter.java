package com.example.lasttryprojectactivity.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.lasttryprojectactivity.Class.Order;
import com.example.lasttryprojectactivity.databinding.ItemOrderBinding;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemOrderBinding binding;

        public ViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Order order) {

            binding.orderIdTextView.setText("Order #" + order.getId());


            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(order.getOrderDate());

            binding.orderDateTextView.setText(formattedDate);
            binding.orderTotalTextView.setText("$" + String.format("%.2f", order.getTotalAmount()));
            binding.orderStatusTextView.setText(order.getStatus());


            int color;
            switch (order.getStatus().toUpperCase()) {
                case "PENDING":
                    color = binding.getRoot().getContext().getResources().getColor(android.R.color.holo_orange_dark);
                    break;
                case "CONFIRMED":
                    color = binding.getRoot().getContext().getResources().getColor(android.R.color.holo_blue_dark);
                    break;
                case "DELIVERED":
                    color = binding.getRoot().getContext().getResources().getColor(android.R.color.holo_green_dark);
                    break;
                default:
                    color = binding.getRoot().getContext().getResources().getColor(android.R.color.darker_gray);
                    break;
            }
            binding.orderStatusTextView.setTextColor(color);
        }
    }
}