package com.example.todoapp;

//Todo_Class with the proper structure
public class Todo {
    // Private vars since we are including getters and setters
    private int id;
    private String title;
    private String description;

    // Constructors
    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
