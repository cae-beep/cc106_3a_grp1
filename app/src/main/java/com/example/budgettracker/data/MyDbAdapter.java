package com.example.budgettracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDbAdapter {
    private MyDbHelper dbHelper;

    public MyDbAdapter(Context context) {
        dbHelper = new MyDbHelper(context);
    }

    // Method to insert a new user
    public long insertUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDbHelper.COLUMN_USERNAME, username);
        contentValues.put(MyDbHelper.COLUMN_PASSWORD, password);

        // Check if the username already exists
        if (checkUserExists(username)) {
            return -1; // Indicates that the user already exists
        }

        return db.insert(MyDbHelper.TABLE_USERS, null, contentValues);
    }

    // Method to check user credentials during login
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(MyDbHelper.TABLE_USERS,
                new String[]{MyDbHelper.COLUMN_PASSWORD},
                MyDbHelper.COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        boolean isValid = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String storedPassword = cursor.getString(0);
                isValid = storedPassword.equals(password);
            }
            cursor.close();
        }
        return isValid;
    }

    // Method to check if the user already exists in the database
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(MyDbHelper.TABLE_USERS,
                new String[]{MyDbHelper.COLUMN_USERNAME},
                MyDbHelper.COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    // New method to insert an expense
    public long insertExpense(double amount, String note, String category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount", amount);
        contentValues.put("note", note);
        contentValues.put("category", category);

        return db.insert("expenses", null, contentValues);
    }
}
