package com.example.mcpa_project_sem22.adapters;

import android.content.Context;
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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        // Use string formatting instead of String.valueOf for better performance
        holder.productPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
    }

    // Add method to update data efficiently
    public void updateData(List<Product> newProductList) {
        int oldSize = productList.size();
        productList.clear();
        notifyItemRangeRemoved(0, oldSize);
        productList.addAll(newProductList);
        notifyItemRangeInserted(0, newProductList.size());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameTextView);
            productPrice = itemView.findViewById(R.id.productPriceTextView);
        }
    }
}