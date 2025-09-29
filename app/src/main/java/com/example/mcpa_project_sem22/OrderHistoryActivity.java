package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.example.mcpa_project_sem22.adapters.OrderHistoryAdapter;
import com.example.mcpa_project_sem22.dao.OrderDAO;
import com.example.mcpa_project_sem22.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderHistoryRecyclerView;
    private TextView emptyOrderHistoryTextView;
    private ProgressBar loadingProgressBar;
    private OrderHistoryAdapter orderHistoryAdapter;
    private OrderDAO orderDAO;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        initializeViews();
        setupRecyclerView();
        orderDAO = new OrderDAO(this);
        loadOrderHistory();
    }

    private void initializeViews() {
        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);
        emptyOrderHistoryTextView = findViewById(R.id.emptyOrderHistoryTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
    }

    private void setupRecyclerView() {
        orderList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(orderList);
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
    }

    private void loadOrderHistory() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        orderHistoryRecyclerView.setVisibility(View.GONE);
        emptyOrderHistoryTextView.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            List<Order> fetchedOrders = orderDAO.getOrderHistoryByUserId(userId);
            if (fetchedOrders.isEmpty()) {
                emptyOrderHistoryTextView.setText("No orders found.");
                emptyOrderHistoryTextView.setVisibility(View.VISIBLE);
            } else {
                orderList.clear();
                orderList.addAll(fetchedOrders);
                orderHistoryAdapter.notifyDataSetChanged();
                orderHistoryRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            emptyOrderHistoryTextView.setText("Please log in to view your order history.");
            emptyOrderHistoryTextView.setVisibility(View.VISIBLE);
        }
        loadingProgressBar.setVisibility(View.GONE);
    }
}