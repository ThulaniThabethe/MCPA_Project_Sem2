package com.example.mcpa_project_sem22.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcpa_project_sem22.R;
import com.example.mcpa_project_sem22.models.Product;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartItems;

    public CartAdapter(List<Product> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.productName.setText(product.getName());
        // Use string formatting instead of String.valueOf for better performance
        holder.productPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
    }

    // Add method to update data efficiently
    public void updateData(List<Product> newCartItems) {
        int oldSize = cartItems.size();
        cartItems.clear();
        notifyItemRangeRemoved(0, oldSize);
        cartItems.addAll(newCartItems);
        notifyItemRangeInserted(0, newCartItems.size());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameTextView);
            productPrice = itemView.findViewById(R.id.priceTextView);
        }
    }
}