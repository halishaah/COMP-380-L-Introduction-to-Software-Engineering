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
    public int checkTitle(String title){
        if (title.trim().length() == 0) {
            return 0;
        }
        return 1;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public int checkDescription(String description){
        if (description.trim().length() == 0) {
            return 0;
        }
        return 1;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
