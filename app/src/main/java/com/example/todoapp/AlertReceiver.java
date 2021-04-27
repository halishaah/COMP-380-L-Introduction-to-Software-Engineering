package com.example.todoapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static com.example.todoapp.App.CHANNEL_ID;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(context);
        String message = "Task \"";
        //this pulls from the extra variables. Could get from the DB if needed;
        //intent.getStringExtra("Name of extra variable"); is how I got the extra variables passed in the intent
        String task_title = intent.getStringExtra("task_title");
        int idNum = intent.getIntExtra("id_num",-1);
        message+=task_title+"\" is due";
        System.out.println(intent.getIntExtra("id_num",-1));
        System.out.println(intent.getStringExtra("task_title"));
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
        //This is the actual notification and setting the message
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Reminder: Task Due!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent1)
                .setAutoCancel(true)
//                Sets when notification goes out.
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .build();
        //Send the notification
        notificationManagerCompat.notify(idNum,notification);
    }
}