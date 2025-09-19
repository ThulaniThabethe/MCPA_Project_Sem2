package com.example.mcpa_project_sem22;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import com.example.mcpa_project_sem22.models.Store;
import com.example.mcpa_project_sem22.Promotion;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "woolworths.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PROMOTIONS = "promotions";
    public static final String TABLE_STORES = "stores";

    // Users Table Columns
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";

    // Promotions Table Columns
    public static final String COLUMN_PROMOTION_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_IMAGE_URL = "image_url";

    // Stores Table Columns
    public static final String COLUMN_STORE_ID = "_id";
    public static final String COLUMN_STORE_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";

    // SQL for creating tables
    private static final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE,"
            + COLUMN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_EMAIL + " TEXT NOT NULL UNIQUE)";

    private static final String SQL_CREATE_PROMOTIONS_TABLE = "CREATE TABLE " + TABLE_PROMOTIONS + "("
            + COLUMN_PROMOTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT NOT NULL,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_START_DATE + " TEXT,"
            + COLUMN_END_DATE + " TEXT,"
            + COLUMN_IMAGE_URL + " TEXT)";

    private static final String SQL_CREATE_STORES_TABLE = "CREATE TABLE " + TABLE_STORES + "("
            + COLUMN_STORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_STORE_NAME + " TEXT NOT NULL,"
            + COLUMN_ADDRESS + " TEXT NOT NULL,"
            + COLUMN_LATITUDE + " REAL,"
            + COLUMN_LONGITUDE + " REAL,"
            + COLUMN_PHONE_NUMBER + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_PROMOTIONS_TABLE);
        db.execSQL(SQL_CREATE_STORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
        // Create tables again
        onCreate(db);
    }

    // Add a new user to the database
    public boolean addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_USERS, null, contentValues);
        db.close();
        return result != -1;
    }

    // Check if a user exists with the given username and password
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    // Check if a username already exists
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    // Check if an email already exists
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    // Add a new promotion to the database
    public boolean addPromotion(String title, String description, String startDate, String endDate, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_START_DATE, startDate);
        contentValues.put(COLUMN_END_DATE, endDate);
        contentValues.put(COLUMN_IMAGE_URL, imageUrl);

        long result = db.insert(TABLE_PROMOTIONS, null, contentValues);
        db.close();
        return result != -1;
    }

    // Get all promotions
    public void addPromotion(Promotion promotion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, promotion.getTitle());
        values.put(COLUMN_DESCRIPTION, promotion.getDescription());
        values.put(COLUMN_START_DATE, promotion.getStartDate());
        values.put(COLUMN_END_DATE, promotion.getEndDate());
        values.put(COLUMN_IMAGE_URL, promotion.getImageUrl());

        db.insert(TABLE_PROMOTIONS, null, values);
        db.close();
    }

    public List<Promotion> getAllPromotions() {
        List<Promotion> promotionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PROMOTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Promotion promotion = new Promotion(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROMOTION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                );
                promotionList.add(promotion);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return promotionList;
    }

    // Store methods
    public void addStore(Store store) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STORE_NAME, store.getName());
        values.put(COLUMN_ADDRESS, store.getAddress());
        values.put(COLUMN_PHONE_NUMBER, store.getPhone()); // Corrected method call
        values.put(COLUMN_LATITUDE, store.getLatitude());
        values.put(COLUMN_LONGITUDE, store.getLongitude());

        db.insert(TABLE_STORES, null, values);
        db.close();
    }

    public List<Store> getAllStores() {
        List<Store> storeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Store store = new Store(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                );
                storeList.add(store);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return storeList;
    }
}