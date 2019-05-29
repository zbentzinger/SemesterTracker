package com.wgu.zbentz2.semestertracker.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.wgu.zbentz2.semestertracker.R;

public class NotificationUtils {

    public final static long MILLISECONDS_IN_DAY = 86400000;
    public final static long MILLISECONDS_IN_WEEK = MILLISECONDS_IN_DAY * 7;

    static int notificationID;
    static int requestCode;

    public static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                context.getResources().getString(R.string.channel_id),
                context.getResources().getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            );

            channel.setDescription(
                context.getResources().getString(R.string.channel_description)
            );

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);

        }

    }

    public static void scheduleNotification(Context context, String title, String content, long triggerDate) {

        Intent intent = new Intent(context, AlarmReceiver.class);

        intent.putExtra("NotificationTitle", title);
        intent.putExtra("NotificationContent", content);

        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode++, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerDate, sender);

    }

    public static void sendNotification(Context context, Intent intent) {

        String channel = context.getResources().getString(R.string.channel_id);
        String title = intent.getExtras().getString("NotificationTitle");
        String content = intent.getExtras().getString("NotificationContent");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(notificationID++, builder.build());

    }

}
