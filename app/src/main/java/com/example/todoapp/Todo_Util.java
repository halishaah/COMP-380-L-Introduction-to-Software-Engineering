package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Todo_Util extends DB_Util {
    // Constructors
    public Todo_Util(Context context) {
        super(context);
    }

    // @name createTodo
    // @desc creates a new item
    public int createTodo(Todo todo) {
        ContentValues val = new ContentValues();
        val.put("title", todo.getTitle());
        val.put("description", todo.getDescription());
        val.put("date",todo.getDate());

        SQLiteDatabase db = this.getWritableDatabase();
        int dbID = (int) db.insert("Todo", null, val);
        db.close();

        return dbID;
    }

    // @name getTodos
    // @desc gets all items
    public ArrayList<Todo> getTodos(int sort) {
        ArrayList<Todo> todos = new ArrayList<>();
        String e = "SELECT * FROM todo ORDER BY id ASC";
        if(sort == 1)
            e = "SELECT * FROM todo ORDER BY title ASC";
        else if(sort == 2)
            e = "SELECT * FROM todo ORDER BY date ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(e, null);

        if (cursor.moveToFirst()) {
            do {
                // Get id of row the cursor is located on
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));

                // Get title of row the cursor is located on
                String title = cursor.getString(cursor.getColumnIndex("title"));

                // Get description of row the cursor is located on
                String desc = cursor.getString(cursor.getColumnIndex("description"));

                String date = cursor.getString(cursor.getColumnIndex("date"));
                // Add the todoitem to the array list
                Todo todo = new Todo(title, desc,date);

                // Set id
                todo.setId(id);

                // Add to list
                todos.add(todo);
            } while (cursor.moveToNext());

            // This avoids memory leaks
            cursor.close();

            db.close();
        }
        return todos;
    }

    // @name getTodo
    // @desc get a single item
    public Todo getTodo(int id) {
        Todo todo = null;
        String e = "SELECT * FROM Todo WHERE id=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(e, null);

        if (cursor.moveToFirst()) {
            // Get id of row the cursor is located on
            int todoId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));

            // Get title of row the cursor is located on
            String title = cursor.getString(cursor.getColumnIndex("title"));

            // Get description of row the cursor is located on
            String desc = cursor.getString(cursor.getColumnIndex("description"));
            String date = cursor.getString(cursor.getColumnIndex("date"));

            // Add the todoitem to the array list
            todo = new Todo(title, desc,date);


            // Set id
            todo.setId(todoId);
        }
        cursor.close();
        db.close();

        return todo;
    }

    // @name updateTodo
    // @desc update an item
    public boolean updateTodo(Todo todo) {
        ContentValues val = new ContentValues();
        val.put("title", todo.getTitle());
        val.put("description", todo.getDescription());
        val.put("date",todo.getDate());
        SQLiteDatabase db = this.getWritableDatabase();

        boolean greatSuccess = db.update("Todo", val, "id='"+todo.getId()+"'", null) > 0;
        db.close();
        return greatSuccess;
    }

    // @name deleteTodo
    // @desc delete an item
    public boolean deleteTodo(int id) {
        boolean deleted;
        SQLiteDatabase db = this.getWritableDatabase();
        deleted = db.delete("Todo", "id='" + id + "'", null) > 0;
        db.close();
        return deleted;
    }
}














