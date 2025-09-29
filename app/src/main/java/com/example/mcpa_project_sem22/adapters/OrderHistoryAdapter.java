package com.example.mcpa_project_sem22.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcpa_project_sem22.R;
import com.example.mcpa_project_sem22.models.Order; // Assuming an Order model exists or will be created

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderHistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.orderDateTextView.setText("Date: " + order.getOrderDate());
        holder.orderTotalTextView.setText("Total: R" + String.format("%.2f", order.getTotalAmount()));
        // You might want to add more details like items in the order
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView orderDateTextView;
        TextView orderTotalTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderTotalTextView = itemView.findViewById(R.id.orderTotalTextView);
        }
    }

    public void setOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }
}