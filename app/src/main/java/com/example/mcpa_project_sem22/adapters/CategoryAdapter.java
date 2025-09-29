package com.example.mcpa_project_sem22.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mcpa_project_sem22.R;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<String> {

    public CategoryAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }

        String category = getItem(position);

        TextView categoryName = convertView.findViewById(R.id.categoryNameTextView);
        categoryName.setText(category);

        return convertView;
    }
}