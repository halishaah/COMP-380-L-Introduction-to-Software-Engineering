package com.example.todoapp;

import junit.framework.TestCase;

public class TodoTest1 extends TestCase {
    Todo todo = new Todo("Unit Test Title", "Unit Test Description", "05/12/2021");
    public void testGetDate() {
        assertEquals(todo.getDate(),"05/12/2021");
    }

    public void testSetDate() {
        todo.setDate("12/31/2021");
        assertEquals(todo.getDate(),"12/31/2021");
    }

    public void testGetTitle() {
        assertEquals(todo.getTitle(),"Unit Test Title");
    }

    public void testCheckTitle() {
        assertEquals(todo.checkTitle(""),0);
        assertEquals(todo.checkTitle("Title"),1);
    }

    public void testSetTitle() {
        todo.setTitle("Unit Title part 2");
        assertEquals(todo.getTitle(),"Unit Title part 2");
    }

    public void testGetDescription() {
        assertEquals(todo.getDescription(),"Unit Test Description");
    }

    public void testCheckDescription() {
        assertEquals(todo.checkDescription(""),0);
        assertEquals(todo.checkDescription("Check again"),1);
    }

    public void testSetDescription() {
        todo.setDescription("New Description");
        assertEquals(todo.getDescription(),"New Description");
    }
}