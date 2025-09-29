package com.example.mcpa_project_sem22.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mcpa_project_sem22.DatabaseHelper;
import com.example.mcpa_project_sem22.models.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO {
    private DatabaseHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Order> getOrderHistoryByUserId(int userId) {
        List<Order> orderHistory = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_ORDER_ID,
                DatabaseHelper.COLUMN_ORDER_USER_ID,
                DatabaseHelper.COLUMN_ORDER_DATE,
                DatabaseHelper.COLUMN_ORDER_TOTAL_AMOUNT
        };

        String selection = DatabaseHelper.COLUMN_ORDER_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_ORDERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                DatabaseHelper.COLUMN_ORDER_DATE + " DESC"
        );

        while (cursor.moveToNext()) {
            int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID));
            long orderDateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DATE));
            double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TOTAL_AMOUNT));

            Order order = new Order(orderId, userId, new Date(orderDateMillis), totalAmount);
            orderHistory.add(order);
        }
        cursor.close();
        db.close();
        return orderHistory;
    }
}