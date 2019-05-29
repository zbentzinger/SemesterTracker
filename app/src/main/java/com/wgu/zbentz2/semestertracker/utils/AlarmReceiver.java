package com.wgu.zbentz2.semestertracker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {

        NotificationUtils.sendNotification(context, intent);

    }

}
