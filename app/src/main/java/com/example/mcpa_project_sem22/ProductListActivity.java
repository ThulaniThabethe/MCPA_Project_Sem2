package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.List;
import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class ProductListActivity extends AppCompatActivity {

    private ImageButton backButton;
    private MaterialButton filterButton;
    private MaterialButton sortButton;
    private MaterialButton viewToggleButton;
    private ChipGroup categoryChipGroup;
    private TextInputEditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        backButton = findViewById(R.id.backButton);
        filterButton = findViewById(R.id.filterButton);
        sortButton = findViewById(R.id.sortButton);
        viewToggleButton = findViewById(R.id.viewToggleButton);
        categoryChipGroup = findViewById(R.id.categoryChipGroup);
        searchEditText = findViewById(R.id.searchEditText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductListActivity.this, "Filter button clicked", Toast.LENGTH_SHORT).show();
                // Implement filter logic here
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductListActivity.this, "Sort button clicked", Toast.LENGTH_SHORT).show();
                // Implement sort logic here
            }
        });

        viewToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductListActivity.this, "View toggle button clicked", Toast.LENGTH_SHORT).show();
                // Implement view toggle logic here (e.g., switch between grid and list)
            }
        });

        categoryChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (!checkedIds.isEmpty()) {
                    Chip chip = findViewById(checkedIds.get(0));
                    if (chip != null) {
                        Toast.makeText(ProductListActivity.this, "Category selected: " + chip.getText(), Toast.LENGTH_SHORT).show();
                        // Implement category filter logic here
                    }
                } else {
                    Toast.makeText(ProductListActivity.this, "No category selected", Toast.LENGTH_SHORT).show();
                    // Implement logic for no category selected (e.g., show all products)
                }
            }
        });

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductListActivity.this, "Search bar clicked", Toast.LENGTH_SHORT).show();
                // Implement search logic here
            }
        });
    }
}