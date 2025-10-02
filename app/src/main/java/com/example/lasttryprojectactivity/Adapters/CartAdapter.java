package com.example.lasttryprojectactivity.Adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.lasttryprojectactivity.Class.CartItem;
import com.example.lasttryprojectactivity.R;
import com.example.lasttryprojectactivity.RoomDatabase.AppViewModel;
import com.example.lasttryprojectactivity.databinding.ItemCartBinding;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartItem> cartItems;
    private AppViewModel viewModel;

    public CartAdapter(List<CartItem> cartItems, AppViewModel viewModel) {
        this.cartItems = cartItems;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cartItems != null && position < cartItems.size()) {
            CartItem cartItem = cartItems.get(position);
            if (cartItem != null) {
                holder.bind(cartItem);
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCartBinding binding;

        public ViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItem cartItem) {
            if (binding != null && cartItem != null) {
                binding.nameTextView.setText(cartItem.getMenuItemName() != null ? cartItem.getMenuItemName() : "");
                binding.priceTextView.setText("$" + String.format("%.2f", cartItem.getPrice()));
                binding.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
                binding.totalTextView.setText("$" + String.format("%.2f", cartItem.getTotalPrice()));


                try {
                    if (cartItem.getImageResourceId() != 0) {
                        binding.imageView.setImageResource(cartItem.getImageResourceId());
                    } else {
                        binding.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                } catch (Exception e) {
                    binding.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                }


                binding.increaseButton.setOnClickListener(v -> {
                    if (viewModel != null && cartItem != null) {
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                        viewModel.updateCartItem(cartItem);
                        notifyItemChanged(getAdapterPosition());
                    }
                });

                binding.decreaseButton.setOnClickListener(v -> {
                    if (viewModel != null && cartItem != null && cartItem.getQuantity() > 1) {
                        cartItem.setQuantity(cartItem.getQuantity() - 1);
                        viewModel.updateCartItem(cartItem);
                        notifyItemChanged(getAdapterPosition());
                    }
                });

                binding.removeButton.setOnClickListener(v -> {
                    if (viewModel != null && cartItem != null) {

                        LayoutInflater inflater = LayoutInflater.from(v.getContext());
                        View dialogView = inflater.inflate(R.layout.custom_delete_dialog, null);

                        AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                                .setView(dialogView)
                                .create();


                        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

                        btnCancel.setOnClickListener(view -> dialog.dismiss());

                        btnConfirm.setOnClickListener(view -> {

                            viewModel.removeFromCart(cartItem);
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION && cartItems != null) {
                                cartItems.remove(position);
                                notifyItemRemoved(position);
                            }
                            dialog.dismiss();
                        });

                        dialog.show();
                    }
                });


            }
        }
    }
}