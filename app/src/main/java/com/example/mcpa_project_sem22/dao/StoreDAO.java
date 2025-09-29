package com.example.mcpa_project_sem22.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.mcpa_project_sem22.DatabaseHelper;
import com.example.mcpa_project_sem22.models.Store;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {
    private static final String TAG = "StoreDAO";
    private DatabaseHelper dbHelper;

    public StoreDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add a new store
    public long addStore(Store store) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("name", store.getName());
        values.put("address", store.getAddress());
        values.put("city", store.getCity());
        values.put("province", store.getProvince());
        values.put("postal_code", store.getPostalCode());
        values.put("phone", store.getPhone());
        values.put("email", store.getEmail());
        values.put("latitude", store.getLatitude());
        values.put("longitude", store.getLongitude());
        values.put("opening_hours", store.getOpeningHours());
        values.put("is_active", store.isActive() ? 1 : 0);
        values.put("store_type", store.getStoreType());
        values.put("image_url", store.getImageUrl());
        
        long result = db.insert("stores", null, values);
        db.close();
        
        if (result != -1) {
            Log.d(TAG, "Store added successfully: " + store.getName());
        } else {
            Log.e(TAG, "Failed to add store: " + store.getName());
        }
        
        return result;
    }

    // Get all active stores
    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT * FROM stores WHERE is_active = 1 ORDER BY name";
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                Store store = createStoreFromCursor(cursor);
                stores.add(store);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return stores;
    }

    // Get stores by province
    public List<Store> getStoresByProvince(String province) {
        List<Store> stores = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT * FROM stores WHERE province = ? AND is_active = 1 ORDER BY name";
        Cursor cursor = db.rawQuery(query, new String[]{province});
        
        if (cursor.moveToFirst()) {
            do {
                Store store = createStoreFromCursor(cursor);
                stores.add(store);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return stores;
    }

    // Get stores by city
    public List<Store> getStoresByCity(String city) {
        List<Store> stores = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT * FROM stores WHERE city = ? AND is_active = 1 ORDER BY name";
        Cursor cursor = db.rawQuery(query, new String[]{city});
        
        if (cursor.moveToFirst()) {
            do {
                Store store = createStoreFromCursor(cursor);
                stores.add(store);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return stores;
    }

    // Get store by ID
    public Store getStoreById(int storeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Store store = null;
        
        String query = "SELECT * FROM stores WHERE store_id = ? AND is_active = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(storeId)});
        
        if (cursor.moveToFirst()) {
            store = createStoreFromCursor(cursor);
        }
        
        cursor.close();
        db.close();
        return store;
    }

    // Search stores by name or address
    public List<Store> searchStores(String searchTerm) {
        List<Store> stores = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT * FROM stores WHERE (name LIKE ? OR address LIKE ? OR city LIKE ?) AND is_active = 1 ORDER BY name";
        String searchPattern = "%" + searchTerm + "%";
        Cursor cursor = db.rawQuery(query, new String[]{searchPattern, searchPattern, searchPattern});
        
        if (cursor.moveToFirst()) {
            do {
                Store store = createStoreFromCursor(cursor);
                stores.add(store);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return stores;
    }

    // Get nearby stores within a certain radius (in kilometers)
    public List<Store> getNearbyStores(double latitude, double longitude, double radiusKm) {
        List<Store> allStores = getAllStores();
        List<Store> nearbyStores = new ArrayList<>();
        
        for (Store store : allStores) {
            double distance = store.distanceTo(latitude, longitude);
            if (distance <= radiusKm) {
                nearbyStores.add(store);
            }
        }
        
        // Sort by distance (closest first)
        nearbyStores.sort((s1, s2) -> {
            double dist1 = s1.distanceTo(latitude, longitude);
            double dist2 = s2.distanceTo(latitude, longitude);
            return Double.compare(dist1, dist2);
        });
        
        return nearbyStores;
    }

    // Update store information
    public boolean updateStore(Store store) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("name", store.getName());
        values.put("address", store.getAddress());
        values.put("city", store.getCity());
        values.put("province", store.getProvince());
        values.put("postal_code", store.getPostalCode());
        values.put("phone", store.getPhone());
        values.put("email", store.getEmail());
        values.put("latitude", store.getLatitude());
        values.put("longitude", store.getLongitude());
        values.put("opening_hours", store.getOpeningHours());
        values.put("is_active", store.isActive() ? 1 : 0);
        values.put("store_type", store.getStoreType());
        values.put("image_url", store.getImageUrl());
        
        int rowsAffected = db.update("stores", values, "store_id = ?", 
                                   new String[]{String.valueOf(store.getId())});
        db.close();
        
        boolean success = rowsAffected > 0;
        if (success) {
            Log.d(TAG, "Store updated successfully: " + store.getName());
        } else {
            Log.e(TAG, "Failed to update store: " + store.getName());
        }
        
        return success;
    }

    // Soft delete store (set is_active to false)
    public boolean deleteStore(int storeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_active", 0);
        
        int rowsAffected = db.update("stores", values, "store_id = ?", 
                                   new String[]{String.valueOf(storeId)});
        db.close();
        
        boolean success = rowsAffected > 0;
        if (success) {
            Log.d(TAG, "Store deleted successfully: " + storeId);
        } else {
            Log.e(TAG, "Failed to delete store: " + storeId);
        }
        
        return success;
    }

    // Get distinct provinces
    public List<String> getAllProvinces() {
        List<String> provinces = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT DISTINCT province FROM stores WHERE is_active = 1 AND province IS NOT NULL ORDER BY province";
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                provinces.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return provinces;
    }

    // Get distinct cities for a province
    public List<String> getCitiesByProvince(String province) {
        List<String> cities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT DISTINCT city FROM stores WHERE province = ? AND is_active = 1 AND city IS NOT NULL ORDER BY city";
        Cursor cursor = db.rawQuery(query, new String[]{province});
        
        if (cursor.moveToFirst()) {
            do {
                cities.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return cities;
    }

    // Helper method to create Store object from cursor
    private Store createStoreFromCursor(Cursor cursor) {
        Store store = new Store();
        store.setId(cursor.getInt(cursor.getColumnIndexOrThrow("store_id")));
        store.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        store.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        store.setCity(cursor.getString(cursor.getColumnIndexOrThrow("city")));
        store.setProvince(cursor.getString(cursor.getColumnIndexOrThrow("province")));
        store.setPostalCode(cursor.getString(cursor.getColumnIndexOrThrow("postal_code")));
        store.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
        store.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        store.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
        store.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
        store.setOpeningHours(cursor.getString(cursor.getColumnIndexOrThrow("opening_hours")));
        store.setActive(cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1);
        store.setStoreType(cursor.getString(cursor.getColumnIndexOrThrow("store_type")));
        store.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("image_url")));
        return store;
    }
}