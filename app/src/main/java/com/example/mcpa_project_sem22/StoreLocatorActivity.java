package com.example.mcpa_project_sem22;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcpa_project_sem22.adapters.StoreAdapter;
import com.example.mcpa_project_sem22.models.Store;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.location.LocationListener;

public class StoreLocatorActivity extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private RecyclerView storesRecyclerView;
    private StoreAdapter storeAdapter;
    private List<Store> allStores;
    private DatabaseHelper databaseHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);

        databaseHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize with some dummy data if the database is empty
        if (databaseHelper.getAllStores().isEmpty()) {
            databaseHelper.addStore(new Store(0, "Woolworths Metro Martin Place", "Shop 1, 60 Martin Pl, Sydney NSW 2000", "(02) 9255 9012", -33.868704, 151.209295));
            databaseHelper.addStore(new Store(0, "Woolworths Surry Hills", "Cleveland St & Crown St, Surry Hills NSW 2010", "(02) 9255 3456", -33.886700, 151.210000));
            databaseHelper.addStore(new Store(0, "Woolworths Bondi Junction", "Westfield Bondi Junction, 500 Oxford St, Bondi Junction NSW 2022", "(02) 9255 7890", -33.891600, 151.250000));
        }

        allStores = databaseHelper.getAllStores();

        storesRecyclerView = findViewById(R.id.stores_recycler_view);
        storesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        storeAdapter = new StoreAdapter(allStores);
        storesRecyclerView.setAdapter(storeAdapter);

        Button findNearestStoreButton = findViewById(R.id.findNearestStoreButton);
        findNearestStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNearestStore();
            }
        });

        Spinner areaSpinner = findViewById(R.id.areaSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.store_areas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter);
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedArea = parent.getItemAtPosition(position).toString();
                filterStoresByArea(selectedArea);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        requestLocationPermission();
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, find nearest store
                findNearestStore();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findNearestStore() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lastKnownLocation = location;
                            // Sort stores by distance to current location
                            Collections.sort(allStores, new Comparator<Store>() {
                                @Override
                                public int compare(Store s1, Store s2) {
                                    float[] results1 = new float[1];
                                    Location.distanceBetween(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                                            s1.getLatitude(), s1.getLongitude(), results1);
                                    float[] results2 = new float[1];
                                    Location.distanceBetween(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                                            s2.getLatitude(), s2.getLongitude(), results2);
                                    return Float.compare(results1[0], results2[0]);
                                }
                            });
                            storeAdapter.notifyDataSetChanged();
                            Toast.makeText(StoreLocatorActivity.this, "Stores sorted by distance", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StoreLocatorActivity.this, "Could not retrieve current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void filterStoresByArea(String area) {
        List<Store> filteredStores = new ArrayList<>();
        if (area.equals("All Areas")) {
            filteredStores.addAll(allStores);
        }
        else {
            for (Store store : allStores) {
                if (store.getAddress().toLowerCase().contains(area.toLowerCase())) {
                    filteredStores.add(store);
                }
            }
        }
        storeAdapter = new StoreAdapter(filteredStores);
        storesRecyclerView.setAdapter(storeAdapter);
        storeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}