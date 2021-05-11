package com.example.todoapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class TodoTest {

    String titleNew = "Lazy";
    String descriptionNew = "Let just do nothing for day.";
    String dateNew = "05/13/2021";

    Todo t1 = new Todo("Running", "Plan to exercise on my day off.", "05/11/2021");

    @Test
    public void getDate1() {
        assertEquals("05/11/2021", t1.getDate());
    }

    @Test
    public void setDate1() {
        t1.setDate(dateNew);
        assertEquals(dateNew, t1.getDate());
    }

    @Test
    public void getTitle1() {
        assertEquals("Running", t1.getTitle());
    }

    @Test
    public void setTitle1() {
        t1.setTitle(titleNew);
        assertEquals(titleNew, t1.getTitle());
    }

    @Test
    public void getDescription1() {
        assertEquals("Plan to exercise on my day off.", t1.getDescription());


    }

}