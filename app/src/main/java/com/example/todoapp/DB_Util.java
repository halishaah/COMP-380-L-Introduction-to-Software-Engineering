package com.example.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Util extends SQLiteOpenHelper {
    // Define database version
    private static final int DB_VERSION = 1;

    // Define database name
    private static final String DB_NAME = "TodoAppDB";

    // Constructors
    public DB_Util(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates Database table
        String e = "CREATE TABLE Todo ( id INTEGER PRIMARY KEY AUTOINCREMENT, title Text, description Text, date Text)";
        db.execSQL(e);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Updates Databalse table
        String e = "DROP TABLE IF EXISTS Todo";
        db.execSQL(e);
        onCreate(db);
    }
}
