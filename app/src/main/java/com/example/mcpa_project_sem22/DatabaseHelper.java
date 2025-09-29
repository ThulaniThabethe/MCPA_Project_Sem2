package com.example.mcpa_project_sem22;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "woolworths.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseHelper";
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_USER_ID = "user_id";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDER_TOTAL_AMOUNT = "total_amount";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            executeSqlScript(db, "database/woolworths.db.sql");
            insertSampleData(db); // Insert sample data after schema creation
            Log.d(TAG, "Database created successfully and sample data inserted");
        } catch (IOException e) {
            Log.e(TAG, "Error creating database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // Drop all tables
        db.execSQL("DROP TABLE IF EXISTS promotions");
        db.execSQL("DROP TABLE IF EXISTS wishlist");
        db.execSQL("DROP TABLE IF EXISTS reviews");
        db.execSQL("DROP TABLE IF EXISTS payments");
        db.execSQL("DROP TABLE IF EXISTS order_items");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS delivery_addresses");
        db.execSQL("DROP TABLE IF EXISTS delivery_slots");
        db.execSQL("DROP TABLE IF EXISTS product_variants");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS user_addresses");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS stores"); // Add stores table to drop list
        
        // Recreate database
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        Log.d(TAG, "Inserting sample data...");

        // Insert sample users
        ContentValues userValues = new ContentValues();
        userValues.put("email", "test@example.com");
        userValues.put("password_hash", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"); // 'password' hashed with SHA-256
        userValues.put("first_name", "John");
        userValues.put("last_name", "Doe");
        userValues.put("phone_number", "0412345678");
        userValues.put("date_of_birth", "1990-01-01");
        db.insert("users", null, userValues);

        // Insert sample categories
        ContentValues categoryValues = new ContentValues();
        categoryValues.put("name", "Fresh Food");
        categoryValues.put("description", "Fresh fruits, vegetables, meat, and dairy.");
        db.insert("categories", null, categoryValues);

        categoryValues.clear();
        categoryValues.put("name", "Pantry");
        categoryValues.put("description", "Pasta, rice, canned goods, and baking supplies.");
        db.insert("categories", null, categoryValues);

        categoryValues.clear();
        categoryValues.put("name", "Drinks");
        categoryValues.put("description", "Juices, soft drinks, and water.");
        db.insert("categories", null, categoryValues);

        // Insert sample products
        ContentValues productValues = new ContentValues();
        productValues.put("name", "Apple (Red)");
        productValues.put("description", "Crisp and juicy red apples.");
        productValues.put("price", 3.50);
        productValues.put("category_id", 1); // Fresh Food
        productValues.put("brand", "Local Farms");
        productValues.put("unit", "kg");
        productValues.put("stock_quantity", 100);
        productValues.put("image_url", "https://www.woolworths.com.au/shop/productdetails/22606/red-apple");
        db.insert("products", null, productValues);

        productValues.clear();
        productValues.put("name", "Milk (Full Cream)");
        productValues.put("description", "Fresh full cream milk, 2L.");
        productValues.put("price", 3.00);
        productValues.put("category_id", 1); // Fresh Food
        productValues.put("brand", "Dairy Farmers");
        productValues.put("unit", "2L");
        productValues.put("stock_quantity", 50);
        productValues.put("image_url", "https://www.woolworths.com.au/shop/productdetails/300001/dairy-farmers-full-cream-milk");
        db.insert("products", null, productValues);

        productValues.clear();
        productValues.put("name", "Pasta (Spaghetti)");
        productValues.put("description", "Italian spaghetti, 500g.");
        productValues.put("price", 2.00);
        productValues.put("category_id", 2); // Pantry
        productValues.put("brand", "Barilla");
        productValues.put("unit", "500g");
        productValues.put("stock_quantity", 200);
        productValues.put("image_url", "https://www.woolworths.com.au/shop/productdetails/10860/barilla-spaghetti-pasta");
        db.insert("products", null, productValues);

        // Insert sample stores (Woolworths locations)
        ContentValues storeValues = new ContentValues();
        storeValues.put("name", "Woolworths Sydney Metro");
        storeValues.put("address", "123 George St");
        storeValues.put("city", "Sydney");
        storeValues.put("province", "NSW");
        storeValues.put("postal_code", "2000");
        storeValues.put("phone", "0291234567");
        storeValues.put("email", "sydney.metro@woolworths.com.au");
        storeValues.put("latitude", -33.8688);
        storeValues.put("longitude", 151.2093);
        storeValues.put("opening_hours", "Mon-Fri: 7am-9pm, Sat-Sun: 8am-8pm");
        storeValues.put("is_active", 1);
        storeValues.put("store_type", "Supermarket");
        storeValues.put("image_url", "https://www.woolworths.com.au/shop/storelocator/nsw-sydney-metro-2000");
        db.insert("stores", null, storeValues);

        storeValues.clear();
        storeValues.put("name", "Woolworths Melbourne Central");
        storeValues.put("address", "200 Lonsdale St");
        storeValues.put("city", "Melbourne");
        storeValues.put("province", "VIC");
        storeValues.put("postal_code", "3000");
        storeValues.put("phone", "0398765432");
        storeValues.put("email", "melbourne.central@woolworths.com.au");
        storeValues.put("latitude", -37.8124);
        storeValues.put("longitude", 144.9631);
        storeValues.put("opening_hours", "Mon-Fri: 7am-10pm, Sat-Sun: 8am-9pm");
        storeValues.put("is_active", 1);
        storeValues.put("store_type", "Supermarket");
        storeValues.put("image_url", "https://www.woolworths.com.au/shop/storelocator/vic-melbourne-central-3000");
        db.insert("stores", null, storeValues);

        // Insert sample promotions
        ContentValues promoValues = new ContentValues();
        promoValues.put("code", "SAVE10");
        promoValues.put("description", "Get $10 off your next order over $50.");
        promoValues.put("discount_type", "amount");
        promoValues.put("discount_value", 10.00);
        promoValues.put("min_order_amount", 50.00);
        promoValues.put("start_date", "2023-01-01");
        promoValues.put("end_date", "2023-12-31");
        promoValues.put("is_active", 1);
        db.insert("promotions", null, promoValues);

        promoValues.clear();
        promoValues.put("code", "FREEDELIVERY");
        promoValues.put("description", "Free delivery on all orders.");
        promoValues.put("discount_type", "delivery");
        promoValues.put("discount_value", 0.00);
        promoValues.put("min_order_amount", 0.00);
        promoValues.put("start_date", "2023-01-01");
        promoValues.put("end_date", "2023-12-31");
        promoValues.put("is_active", 1);
        db.insert("promotions", null, promoValues);

        Log.d(TAG, "Sample data insertion complete.");
    }

    private void executeSqlScript(SQLiteDatabase db, String sqlFile) throws IOException {
        InputStream inputStream = context.getAssets().open(sqlFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        
        StringBuilder sql = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            // Skip comments and empty lines
            line = line.trim();
            if (line.isEmpty() || line.startsWith("--")) {
                continue;
            }
            
            sql.append(line).append(" ");
            
            // Execute statement when we hit a semicolon
            if (line.endsWith(";")) {
                String statement = sql.toString().trim();
                if (!statement.isEmpty()) {
                    try {
                        db.execSQL(statement);
                        Log.d(TAG, "Executed: " + statement.substring(0, Math.min(50, statement.length())) + "...");
                    } catch (Exception e) {
                        Log.e(TAG, "Error executing SQL: " + statement, e);
                    }
                }
                sql.setLength(0);
            }
        }
        
        reader.close();
        inputStream.close();
    }
}