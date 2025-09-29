package com.example.mcpa_project_sem22.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.mcpa_project_sem22.DatabaseHelper;
import com.example.mcpa_project_sem22.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String TAG = "ProductDAO";
    private DatabaseHelper dbHelper;

    public ProductDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add a new product
    public long addProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long productId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("name", product.getName());
            values.put("description", product.getDescription());
            values.put("price", product.getPrice());
            values.put("discounted_price", product.getDiscountedPrice());
            values.put("category_id", product.getCategoryId());
            values.put("brand", product.getBrand());
            values.put("unit", product.getUnit());
            values.put("stock_quantity", product.getStockQuantity());
            values.put("min_order_quantity", product.getMinOrderQuantity());
            values.put("max_order_quantity", product.getMaxOrderQuantity());
            values.put("image_url", product.getImageUrl());
            values.put("is_active", product.isActive() ? 1 : 0);

            productId = db.insert("products", null, values);
            Log.d(TAG, "Product added with ID: " + productId);
        } catch (Exception e) {
            Log.e(TAG, "Error adding product", e);
        } finally {
            db.close();
        }

        return productId;
    }

    // Get all products
    public List<Product> getAllProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> products = new ArrayList<>();

        try {
            String query = "SELECT p.*, c.name as category_name FROM products p " +
                          "LEFT JOIN categories c ON p.category_id = c.category_id " +
                          "WHERE p.is_active = 1 ORDER BY p.name";
            
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                Product product = createProductFromCursor(cursor);
                products.add(product);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting all products", e);
        } finally {
            db.close();
        }

        return products;
    }

    // Get products by category
    public List<Product> getProductsByCategory(int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> products = new ArrayList<>();

        try {
            String query = "SELECT p.*, c.name as category_name FROM products p " +
                          "LEFT JOIN categories c ON p.category_id = c.category_id " +
                          "WHERE p.category_id = ? AND p.is_active = 1 ORDER BY p.name";
            
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});

            while (cursor.moveToNext()) {
                Product product = createProductFromCursor(cursor);
                products.add(product);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting products by category", e);
        } finally {
            db.close();
        }

        return products;
    }

    // Get product by ID
    public Product getProductById(int productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Product product = null;

        try {
            String query = "SELECT p.*, c.name as category_name FROM products p " +
                          "LEFT JOIN categories c ON p.category_id = c.category_id " +
                          "WHERE p.product_id = ? AND p.is_active = 1";
            
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

            if (cursor.moveToFirst()) {
                product = createProductFromCursor(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting product by ID", e);
        } finally {
            db.close();
        }

        return product;
    }

    // Search products by name
    public List<Product> searchProducts(String searchTerm) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> products = new ArrayList<>();

        try {
            String query = "SELECT p.*, c.name as category_name FROM products p " +
                          "LEFT JOIN categories c ON p.category_id = c.category_id " +
                          "WHERE (p.name LIKE ? OR p.description LIKE ? OR p.brand LIKE ?) " +
                          "AND p.is_active = 1 ORDER BY p.name";
            
            String searchPattern = "%" + searchTerm + "%";
            Cursor cursor = db.rawQuery(query, new String[]{searchPattern, searchPattern, searchPattern});

            while (cursor.moveToNext()) {
                Product product = createProductFromCursor(cursor);
                products.add(product);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error searching products", e);
        } finally {
            db.close();
        }

        return products;
    }

    // Update product
    public boolean updateProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;

        try {
            ContentValues values = new ContentValues();
            values.put("name", product.getName());
            values.put("description", product.getDescription());
            values.put("price", product.getPrice());
            values.put("discounted_price", product.getDiscountedPrice());
            values.put("category_id", product.getCategoryId());
            values.put("brand", product.getBrand());
            values.put("unit", product.getUnit());
            values.put("stock_quantity", product.getStockQuantity());
            values.put("min_order_quantity", product.getMinOrderQuantity());
            values.put("max_order_quantity", product.getMaxOrderQuantity());
            values.put("image_url", product.getImageUrl());
            values.put("is_active", product.isActive() ? 1 : 0);

            int rowsAffected = db.update("products", values, "product_id = ?", 
                new String[]{String.valueOf(product.getProductId())});
            success = rowsAffected > 0;
            
            if (success) {
                Log.d(TAG, "Product updated successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating product", e);
        } finally {
            db.close();
        }

        return success;
    }

    // Update stock quantity
    public boolean updateStock(int productId, int newQuantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;

        try {
            ContentValues values = new ContentValues();
            values.put("stock_quantity", newQuantity);

            int rowsAffected = db.update("products", values, "product_id = ?", 
                new String[]{String.valueOf(productId)});
            success = rowsAffected > 0;
            
            if (success) {
                Log.d(TAG, "Stock updated for product ID: " + productId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating stock", e);
        } finally {
            db.close();
        }

        return success;
    }

    // Delete product (soft delete)
    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;

        try {
            ContentValues values = new ContentValues();
            values.put("is_active", 0);

            int rowsAffected = db.update("products", values, "product_id = ?", 
                new String[]{String.valueOf(productId)});
            success = rowsAffected > 0;
            
            if (success) {
                Log.d(TAG, "Product deleted (soft delete) ID: " + productId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deleting product", e);
        } finally {
            db.close();
        }

        return success;
    }

    // Helper method to create Product object from cursor
    private Product createProductFromCursor(Cursor cursor) {
        Product product = new Product();
        
        int productIdIndex = cursor.getColumnIndex("product_id");
        if (productIdIndex >= 0) {
            product.setProductId(cursor.getInt(productIdIndex));
        }
        
        int nameIndex = cursor.getColumnIndex("name");
        if (nameIndex >= 0) {
            product.setName(cursor.getString(nameIndex));
        }
        
        int descriptionIndex = cursor.getColumnIndex("description");
        if (descriptionIndex >= 0) {
            product.setDescription(cursor.getString(descriptionIndex));
        }
        
        int priceIndex = cursor.getColumnIndex("price");
        if (priceIndex >= 0) {
            product.setPrice(cursor.getDouble(priceIndex));
        }
        
        int discountedPriceIndex = cursor.getColumnIndex("discounted_price");
        if (discountedPriceIndex >= 0) {
            product.setDiscountedPrice(cursor.getDouble(discountedPriceIndex));
        }
        
        int categoryIdIndex = cursor.getColumnIndex("category_id");
        if (categoryIdIndex >= 0) {
            product.setCategoryId(cursor.getInt(categoryIdIndex));
        }
        
        int brandIndex = cursor.getColumnIndex("brand");
        if (brandIndex >= 0) {
            product.setBrand(cursor.getString(brandIndex));
        }
        
        int unitIndex = cursor.getColumnIndex("unit");
        if (unitIndex >= 0) {
            product.setUnit(cursor.getString(unitIndex));
        }
        
        int stockQuantityIndex = cursor.getColumnIndex("stock_quantity");
        if (stockQuantityIndex >= 0) {
            product.setStockQuantity(cursor.getInt(stockQuantityIndex));
        }
        
        int minOrderQuantityIndex = cursor.getColumnIndex("min_order_quantity");
        if (minOrderQuantityIndex >= 0) {
            product.setMinOrderQuantity(cursor.getInt(minOrderQuantityIndex));
        }
        
        int maxOrderQuantityIndex = cursor.getColumnIndex("max_order_quantity");
        if (maxOrderQuantityIndex >= 0) {
            product.setMaxOrderQuantity(cursor.getInt(maxOrderQuantityIndex));
        }
        
        int imageUrlIndex = cursor.getColumnIndex("image_url");
        if (imageUrlIndex >= 0) {
            product.setImageUrl(cursor.getString(imageUrlIndex));
        }
        
        int isActiveIndex = cursor.getColumnIndex("is_active");
        if (isActiveIndex >= 0) {
            product.setActive(cursor.getInt(isActiveIndex) == 1);
        }
        
        int createdAtIndex = cursor.getColumnIndex("created_at");
        if (createdAtIndex >= 0) {
            product.setCreatedAt(cursor.getString(createdAtIndex));
        }

        // Set category name if available
        int categoryNameIndex = cursor.getColumnIndex("category_name");
        if (categoryNameIndex >= 0) {
            product.setCategoryName(cursor.getString(categoryNameIndex));
        }

        return product;
    }
}