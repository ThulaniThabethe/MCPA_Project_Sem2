package com.example.mcpa_project_sem22.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.mcpa_project_sem22.DatabaseHelper;
import com.example.mcpa_project_sem22.models.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String TAG = "UserDAO";
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error hashing password", e);
            return password; // Fallback (not recommended for production)
        }
    }

    // Register a new user
    public long registerUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long userId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("email", user.getEmail());
            values.put("password_hash", hashPassword(password));
            values.put("first_name", user.getFirstName());
            values.put("last_name", user.getLastName());
            values.put("phone_number", user.getPhoneNumber());
            values.put("date_of_birth", user.getDateOfBirth());
            values.put("is_active", 1);

            userId = db.insert("users", null, values);
            Log.d(TAG, "User registered with ID: " + userId);
        } catch (Exception e) {
            Log.e(TAG, "Error registering user", e);
        } finally {
            db.close();
        }

        return userId;
    }

    // Login user
    public User loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        try {
            String hashedPassword = hashPassword(password);
            String[] columns = {"user_id", "email", "first_name", "last_name", "phone_number", "date_of_birth"};
            String selection = "email = ? AND password_hash = ? AND is_active = 1";
            String[] selectionArgs = {email, hashedPassword};

            Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                user = new User();
                
                int userIdIndex = cursor.getColumnIndex("user_id");
                if (userIdIndex >= 0) {
                    user.setUserId(cursor.getInt(userIdIndex));
                }
                
                int emailIndex = cursor.getColumnIndex("email");
                if (emailIndex >= 0) {
                    user.setEmail(cursor.getString(emailIndex));
                }
                
                int firstNameIndex = cursor.getColumnIndex("first_name");
                if (firstNameIndex >= 0) {
                    user.setFirstName(cursor.getString(firstNameIndex));
                }
                
                int lastNameIndex = cursor.getColumnIndex("last_name");
                if (lastNameIndex >= 0) {
                    user.setLastName(cursor.getString(lastNameIndex));
                }
                
                int phoneNumberIndex = cursor.getColumnIndex("phone_number");
                if (phoneNumberIndex >= 0) {
                    user.setPhoneNumber(cursor.getString(phoneNumberIndex));
                }
                
                int dateOfBirthIndex = cursor.getColumnIndex("date_of_birth");
                if (dateOfBirthIndex >= 0) {
                    user.setDateOfBirth(cursor.getString(dateOfBirthIndex));
                }

                // Update last login
                updateLastLogin(user.getUserId());
                Log.d(TAG, "User logged in: " + email);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error during login", e);
        } finally {
            db.close();
        }

        return user;
    }

    // Update last login timestamp
    private void updateLastLogin(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("last_login", System.currentTimeMillis());
            db.update("users", values, "user_id = ?", new String[]{String.valueOf(userId)});
        } catch (Exception e) {
            Log.e(TAG, "Error updating last login", e);
        } finally {
            db.close();
        }
    }

    // Check if email exists
    public boolean emailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;

        try {
            String[] columns = {"user_id"};
            String selection = "email = ?";
            String[] selectionArgs = {email};

            Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
            exists = cursor.getCount() > 0;
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error checking email existence", e);
        } finally {
            db.close();
        }

        return exists;
    }

    // Get user by ID
    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        try {
            String[] columns = {"user_id", "email", "first_name", "last_name", "phone_number", "date_of_birth"};
            String selection = "user_id = ? AND is_active = 1";
            String[] selectionArgs = {String.valueOf(userId)};

            Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                user = new User();
                
                int userIdIndex = cursor.getColumnIndex("user_id");
                if (userIdIndex >= 0) {
                    user.setUserId(cursor.getInt(userIdIndex));
                }
                
                int emailIndex = cursor.getColumnIndex("email");
                if (emailIndex >= 0) {
                    user.setEmail(cursor.getString(emailIndex));
                }
                
                int firstNameIndex = cursor.getColumnIndex("first_name");
                if (firstNameIndex >= 0) {
                    user.setFirstName(cursor.getString(firstNameIndex));
                }
                
                int lastNameIndex = cursor.getColumnIndex("last_name");
                if (lastNameIndex >= 0) {
                    user.setLastName(cursor.getString(lastNameIndex));
                }
                
                int phoneNumberIndex = cursor.getColumnIndex("phone_number");
                if (phoneNumberIndex >= 0) {
                    user.setPhoneNumber(cursor.getString(phoneNumberIndex));
                }
                
                int dateOfBirthIndex = cursor.getColumnIndex("date_of_birth");
                if (dateOfBirthIndex >= 0) {
                    user.setDateOfBirth(cursor.getString(dateOfBirthIndex));
                }
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting user by ID", e);
        } finally {
            db.close();
        }

        return user;
    }

    // Update user profile
    public boolean updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;

        try {
            ContentValues values = new ContentValues();
            values.put("first_name", user.getFirstName());
            values.put("last_name", user.getLastName());
            values.put("phone_number", user.getPhoneNumber());
            values.put("date_of_birth", user.getDateOfBirth());

            int rowsAffected = db.update("users", values, "user_id = ?", 
                new String[]{String.valueOf(user.getUserId())});
            success = rowsAffected > 0;
            
            if (success) {
                Log.d(TAG, "User updated successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating user", e);
        } finally {
            db.close();
        }

        return success;
    }

    // Get all users (for admin purposes)
    public List<User> getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<User> users = new ArrayList<>();

        try {
            String[] columns = {"user_id", "email", "first_name", "last_name", "phone_number", "date_of_birth", "created_at", "last_login", "is_active"};
            Cursor cursor = db.query("users", columns, null, null, null, null, "created_at DESC");

            while (cursor.moveToNext()) {
                User user = new User();
                
                int userIdIndex = cursor.getColumnIndex("user_id");
                if (userIdIndex >= 0) {
                    user.setUserId(cursor.getInt(userIdIndex));
                }
                
                int emailIndex = cursor.getColumnIndex("email");
                if (emailIndex >= 0) {
                    user.setEmail(cursor.getString(emailIndex));
                }
                
                int firstNameIndex = cursor.getColumnIndex("first_name");
                if (firstNameIndex >= 0) {
                    user.setFirstName(cursor.getString(firstNameIndex));
                }
                
                int lastNameIndex = cursor.getColumnIndex("last_name");
                if (lastNameIndex >= 0) {
                    user.setLastName(cursor.getString(lastNameIndex));
                }
                
                int phoneNumberIndex = cursor.getColumnIndex("phone_number");
                if (phoneNumberIndex >= 0) {
                    user.setPhoneNumber(cursor.getString(phoneNumberIndex));
                }
                
                int dateOfBirthIndex = cursor.getColumnIndex("date_of_birth");
                if (dateOfBirthIndex >= 0) {
                    user.setDateOfBirth(cursor.getString(dateOfBirthIndex));
                }
                
                users.add(user);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting all users", e);
        } finally {
            db.close();
        }

        return users;
    }
}