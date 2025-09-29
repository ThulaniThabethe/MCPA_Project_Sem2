package com.example.mcpa_project_sem22.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcpa_project_sem22.R;
import com.example.mcpa_project_sem22.models.Store;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    public interface OnStoreClickListener {
        void onStoreClick(Store store);
        void onDirectionsClick(Store store);
    }

    private Context context;
    private List<Store> storeList;
    private OnStoreClickListener listener;

    public StoreAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
    }

    public void setOnStoreClickListener(OnStoreClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.bind(store, listener);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView addressTextView;
        TextView phoneTextView;
        TextView hoursTextView;
        MaterialButton callButton;
        MaterialButton directionsButton;
        MaterialButton viewStoreButton;

        StoreViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.storeNameTextView);
            addressTextView = view.findViewById(R.id.storeAddressTextView);
            phoneTextView = view.findViewById(R.id.storePhoneTextView);
            hoursTextView = view.findViewById(R.id.storeHoursTextView);
            callButton = view.findViewById(R.id.callButton);
            directionsButton = view.findViewById(R.id.directionsButton);
            viewStoreButton = view.findViewById(R.id.viewStoreButton);
        }

        void bind(Store store, OnStoreClickListener listener) {
            nameTextView.setText(store.getName());
            addressTextView.setText(store.getFullAddress());
            phoneTextView.setText(store.getPhone() != null ? store.getPhone() : "No phone available");
            hoursTextView.setText(store.getOpeningHours() != null ? store.getOpeningHours() : "Hours not available");

            // Call button click listener
            callButton.setOnClickListener(v -> {
                if (store.getPhone() != null && !store.getPhone().isEmpty()) {
                    makePhoneCall(store.getPhone());
                } else {
                    Toast.makeText(context, "Phone number not available for this store", Toast.LENGTH_SHORT).show();
                }
            });

            // Directions button click listener
            directionsButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDirectionsClick(store);
                }
            });

            // View store button click listener
            viewStoreButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStoreClick(store);
                }
            });

            // Phone number text click listener (alternative way to call)
            phoneTextView.setOnClickListener(v -> {
                if (store.getPhone() != null && !store.getPhone().isEmpty()) {
                    makePhoneCall(store.getPhone());
                } else {
                    Toast.makeText(context, "Phone number not available for this store", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void makePhoneCall(String phoneNumber) {
            try {
                // Clean the phone number (remove spaces, dashes, etc.)
                String cleanPhoneNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
                
                // Create intent to open phone dialer
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + cleanPhoneNumber));
                
                // Check if there's an app that can handle this intent
                if (callIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(callIntent);
                } else {
                    Toast.makeText(context, "No phone app available", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, "Unable to make call: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}