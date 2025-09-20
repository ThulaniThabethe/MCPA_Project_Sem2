package com.example.mcpa_project_sem22;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PromotionsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView promotionsRecyclerView;
    private PromotionAdapter promotionAdapter;
    private List<Promotion> promotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

        databaseHelper = new DatabaseHelper(this);

        promotionsRecyclerView = findViewById(R.id.promotions_recycler_view);
        promotionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        promotionList = new ArrayList<>();
        promotionAdapter = new PromotionAdapter(promotionList);
        promotionsRecyclerView.setAdapter(promotionAdapter);

        loadPromotions();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.nav_promotions);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(PromotionsActivity.this, MainActivity.class));
                    return true;
                } else if (id == R.id.nav_promotions) {
                    return true;
                } else if (id == R.id.nav_store_locator) {
                    startActivity(new Intent(PromotionsActivity.this, StoreLocatorActivity.class));
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(PromotionsActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void loadPromotions() {
        promotionList.clear();
        promotionList.addAll(databaseHelper.getAllPromotions());
        promotionAdapter.notifyDataSetChanged();
    }
}