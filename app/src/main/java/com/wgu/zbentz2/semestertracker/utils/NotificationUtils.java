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
import com.wgu.zbentz2.semestertracker.ui.MainActivity;

import java.util.Random;

public class NotificationUtils {

    public final static long MILLISECONDS_IN_DAY = 86400000;
    public final static long MILLISECONDS_IN_WEEK = MILLISECONDS_IN_DAY * 7;

    static int notificationID;

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

        PendingIntent sender = PendingIntent.getBroadcast(context, generateRequestCode(), intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerDate, sender);

    }

    public static void sendNotification(Context context, Intent intent) {

        String channel = context.getResources().getString(R.string.channel_id);
        String title = intent.getExtras().getString("NotificationTitle");
        String content = intent.getExtras().getString("NotificationContent");

        Intent mainActivity = new Intent(context, MainActivity.class);

        // Load a specific fragment in the app.
        if (title.contains("Assessment")) {

            mainActivity.putExtra("fragmentID", R.id.nav_assessments);

        } else {

            mainActivity.putExtra("fragmentID", R.id.nav_courses);

        }

        PendingIntent pIntent = PendingIntent.getActivity(context, generateRequestCode(), mainActivity, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pIntent) // Start the app.
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(notificationID++, builder.build());

    }

    public static int generateRequestCode() {
        // This is probably a bad idea due to the 2038 problem...

        Random rand = new Random(System.currentTimeMillis());

        return rand.nextInt();

    }

}
