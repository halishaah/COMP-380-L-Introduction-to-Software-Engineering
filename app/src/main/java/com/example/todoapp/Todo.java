package com.example.todoapp;


//Todo_Class with the proper structure
public class Todo {
    // Private vars since we are including getters and setters
    private int id;
    private String title;
    private String description;
    private int month;
    private int day;
    private int year;
    private String date;

    // Constructors
    public Todo(String title, String description, String date){
        this.title=title;
        this.description=description;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;

    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
