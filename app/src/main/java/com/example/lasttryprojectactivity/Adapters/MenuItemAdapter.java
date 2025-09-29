package com.example.lasttryprojectactivity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import com.example.lasttryprojectactivity.Class.MenuItem;
import com.example.lasttryprojectactivity.R;
import com.example.lasttryprojectactivity.databinding.ItemMenuBinding;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<MenuItem> menuItems;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem);
        void onAddToCartClick(MenuItem menuItem);
    }

    public MenuItemAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public void updateMenuItems(List<MenuItem> newMenuItems) {
        this.menuItems = newMenuItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMenuBinding binding = ItemMenuBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return menuItems != null ? menuItems.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMenuBinding binding;

        public ViewHolder(ItemMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MenuItem menuItem) {
            binding.nameTextView.setText(menuItem.getName());
            binding.descriptionTextView.setText(menuItem.getDescription());
            binding.priceTextView.setText("$" + String.format("%.2f", menuItem.getPrice()));


            if (menuItem.getImageResourceId() != 0) {
                Glide.with(context)
                        .load(menuItem.getImageResourceId())
                        .placeholder(R.drawable.ic_food_placeholder)
                        .error(R.drawable.ic_food_placeholder)
                        .into(binding.imageView);
            } else {
                binding.imageView.setImageResource(R.drawable.ic_food_placeholder);
            }

            binding.cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(menuItem);
                }
            });

            binding.addToCartButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCartClick(menuItem);
                }
            });
        }
    }
}