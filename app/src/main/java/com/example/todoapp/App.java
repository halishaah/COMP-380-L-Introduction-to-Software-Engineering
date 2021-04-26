package com.example.todoapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "reminders";
    public void onCreate(){
        super.onCreate();
        System.out.println("created notif channel");
        createNotificationChannels();
    }
    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_ID,
                    "Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Reminders");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            System.out.println("Notif Channel created");
        }
    }
}
