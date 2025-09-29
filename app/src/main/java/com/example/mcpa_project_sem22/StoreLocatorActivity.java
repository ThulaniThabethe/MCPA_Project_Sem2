package com.example.mcpa_project_sem22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.mcpa_project_sem22.adapters.StoreAdapter;
import com.example.mcpa_project_sem22.dao.StoreDAO;
import com.example.mcpa_project_sem22.models.Store;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class StoreLocatorActivity extends AppCompatActivity implements StoreAdapter.OnStoreClickListener, LocationListener {

    private static final String TAG = "StoreLocatorActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    
    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay myLocationOverlay;
    private LocationManager locationManager;
    private StoreDAO storeDAO;
    private List<Store> allStores;
    private List<Store> filteredStores;
    private StoreAdapter storeAdapter;
    private List<Marker> storeMarkers;
    
    // UI Components
    private TextInputEditText searchEditText;
    private MaterialButton filterButton;
    private MaterialButtonToggleGroup toggleGroup;
    private MaterialButton mapViewButton;
    private MaterialButton listViewButton;
    private RecyclerView storesRecyclerView;
    
    private boolean isMapView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: StoreLocatorActivity started.");
        
        // Initialize OSMDroid configuration
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(getPackageName());
        
        setContentView(R.layout.activity_store_locator);
        
        initializeComponents();
        try {
            setupMap();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error setting up map: " + e.getMessage());
            Toast.makeText(this, "Error setting up map: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish(); // Close activity if map setup fails
        }
        setupRecyclerView();
        setupToggleButtons();
        setupSearch();
        loadStores();
        
        // Initialize location services
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        requestLocationPermission();
        Log.d(TAG, "onCreate: StoreLocatorActivity finished initialization.");
    }

    private void initializeComponents() {
        Log.d(TAG, "initializeComponents: Initializing UI components and DAOs.");
        storeDAO = new StoreDAO(this);
        allStores = new ArrayList<>();
        filteredStores = new ArrayList<>();
        storeMarkers = new ArrayList<>();
        
        searchEditText = findViewById(R.id.searchEditText);
        filterButton = findViewById(R.id.filterButton);
        toggleGroup = findViewById(R.id.toggleGroup);
        mapViewButton = findViewById(R.id.mapViewButton);
        listViewButton = findViewById(R.id.listViewButton);
        storesRecyclerView = findViewById(R.id.storesRecyclerView);
        mapView = findViewById(R.id.mapView);
        
        // Set initial selection
        toggleGroup.check(R.id.mapViewButton);
        Log.d(TAG, "initializeComponents: UI components and DAOs initialized.");
    }

    private void setupMap() {
        Log.d(TAG, "setupMap: Setting up map configuration.");
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        
        mapController = mapView.getController();
        mapController.setZoom(6.0);
        
        // Set initial position to South Africa
        GeoPoint southAfrica = new GeoPoint(-30.5595, 22.9375);
        mapController.setCenter(southAfrica);
        
        // Add my location overlay
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);
        Log.d(TAG, "setupMap: Map configuration complete.");
    }

    private void setupRecyclerView() {
        storeAdapter = new StoreAdapter(this, filteredStores);
        storeAdapter.setOnStoreClickListener(this);
        storesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storesRecyclerView.setAdapter(storeAdapter);
    }

    private void setupToggleButtons() {
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.mapViewButton) {
                    showMapView();
                } else if (checkedId == R.id.listViewButton) {
                    showListView();
                }
            }
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStores(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadStores() {
        Log.d(TAG, "loadStores: Loading stores from database.");
        allStores = storeDAO.getAllStores();
        filteredStores.clear();
        filteredStores.addAll(allStores);
        storeAdapter.notifyDataSetChanged();
        updateMapMarkers();
        Log.d(TAG, "loadStores: Stores loaded and map markers updated. Total stores: " + allStores.size());
    }

    private void filterStores(String query) {
        filteredStores.clear();
        if (query.isEmpty()) {
            filteredStores.addAll(allStores);
        } else {
            for (Store store : allStores) {
                if (store.getName().toLowerCase().contains(query.toLowerCase()) ||
                    store.getCity().toLowerCase().contains(query.toLowerCase()) ||
                    store.getProvince().toLowerCase().contains(query.toLowerCase())) {
                    filteredStores.add(store);
                }
            }
        }
        storeAdapter.notifyDataSetChanged();
        updateMapMarkers();
    }

    private void showMapView() {
        isMapView = true;
        mapView.setVisibility(View.VISIBLE);
        storesRecyclerView.setVisibility(View.GONE);
    }

    private void showListView() {
        isMapView = false;
        mapView.setVisibility(View.GONE);
        storesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void updateMapMarkers() {
        // Clear existing markers
        for (Marker marker : storeMarkers) {
            mapView.getOverlays().remove(marker);
        }
        storeMarkers.clear();
        
        // Add new markers for filtered stores
        for (Store store : filteredStores) {
            GeoPoint storeLocation = new GeoPoint(store.getLatitude(), store.getLongitude());
            
            Marker marker = new Marker(mapView);
            marker.setPosition(storeLocation);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            
            // Added null checks for store name and address
            String storeName = store.getName();
            String storeAddress = store.getFullAddress();

            if (storeName != null) {
                marker.setTitle(storeName);
            } else {
                marker.setTitle("Unknown Store");
            }

            if (storeAddress != null) {
                marker.setSubDescription(storeAddress);
            } else {
                marker.setSubDescription("Unknown Address");
            }
            
            // Set marker click listener
            marker.setOnMarkerClickListener((clickedMarker, mapView) -> {
                showStoreInfo(store);
                return true;
            });
            
            mapView.getOverlays().add(marker);
            storeMarkers.add(marker);
        }
        
        mapView.invalidate();
    }

    private void showStoreInfo(Store store) {
        String storeInfo = store.getName() + "\n" + 
                          store.getFullAddress() + "\n" + 
                          "Phone: " + (store.getPhone() != null ? store.getPhone() : "Not available") + "\n" +
                          "Hours: " + (store.getOpeningHours() != null ? store.getOpeningHours() : "Not available");
        Toast.makeText(this, storeInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStoreClick(Store store) {
        if (isMapView) {
            GeoPoint storeLocation = new GeoPoint(store.getLatitude(), store.getLongitude());
            mapController.animateTo(storeLocation);
            mapController.setZoom(15.0);
            showMapView();
            toggleGroup.check(R.id.mapViewButton);
        } else {
            showStoreInfo(store);
        }
    }

    @Override
    public void onDirectionsClick(Store store) {
        openDirections(store);
    }

    private void openDirections(Store store) {
        String uri = String.format("geo:%f,%f?q=%f,%f(%s)", 
                store.getLatitude(), store.getLongitude(),
                store.getLatitude(), store.getLongitude(),
                store.getName());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Fallback to browser
            String browserUri = String.format("https://www.openstreetmap.org/?mlat=%f&mlon=%f&zoom=15",
                    store.getLatitude(), store.getLongitude());
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(browserUri));
            startActivity(browserIntent);
        }
    }

    private void requestLocationPermission() {
        Log.d(TAG, "requestLocationPermission: Requesting location permission.");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            enableMyLocation();
        }
    }

    private void enableMyLocation() {
        Log.d(TAG, "enableMyLocation: Enabling my location.");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null) { // Added null check for locationManager
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
                    Log.d(TAG, "enableMyLocation: Location updates requested.");
                } catch (SecurityException e) {
                    e.printStackTrace();
                    Log.e(TAG, "SecurityException when requesting location updates: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "enableMyLocation: locationManager is null, cannot request updates.");
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(TAG, "onLocationChanged: Location updated to " + location.getLatitude() + ", " + location.getLongitude());
        if (mapController != null) { // Added null check for mapController
            GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
            mapController.animateTo(currentLocation);
            mapController.setZoom(12.0);
        } else {
            Log.e(TAG, "onLocationChanged: mapController is null, cannot update map view.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: Location permission granted.");
                enableMyLocation();
            } else {
                Log.w(TAG, "onRequestPermissionsResult: Location permission denied.");
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: StoreLocatorActivity resumed.");
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: StoreLocatorActivity paused.");
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: StoreLocatorActivity destroyed.");
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            Log.d(TAG, "onDestroy: Location updates removed.");
        }
    }
}