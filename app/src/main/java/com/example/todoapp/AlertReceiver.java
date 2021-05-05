package com.example.todoapp;

import android.app.AlarmManager;
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
        AlarmManager almManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String message = "Task \"";
        //this pulls from the extra variables. Could get from the DB if needed;
        //intent.getStringExtra("Name of extra variable"); is how I got the extra variables passed in the intent
        String repeat = intent.getStringExtra("frequency");
        String task_title = intent.getStringExtra("task_title");
        int idNum = intent.getIntExtra("id_num",-1);
        message+=task_title+"\" is due";

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
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND,0);
        //check if repeating and set the next pendingIntent
        //use FLAG_CANCEL_CURRENT to replace the previous pending intent with the new one
        if(repeat.equals("Daily")){
            c.add(Calendar.DATE,1);
//            For Testing on shorter intervals
//            c.add(Calendar.MINUTE,1);
            System.out.println(c.getTime().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,idNum, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            almManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
        else if(repeat.equals("Weekly")){
            c.add(Calendar.DATE,7);
//            For testing on shorter intervals
//            c.add(Calendar.MINUTE,2);
            System.out.println(c.getTime().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,idNum, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            almManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
        else if(repeat.equals("Monthly")){
            c.add(Calendar.MONTH,1);
//            For testing on shorter periods
//            c.add(Calendar.MINUTE,3);
            System.out.println(c.getTime().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,idNum, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            almManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }

    }
}