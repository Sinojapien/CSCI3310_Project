package edu.cuhk.csci3310.project.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class restartService extends BroadcastReceiver {

    // load from user settings
    private boolean restartOnBoot = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) && restartOnBoot){
            Intent notificationIntent = new Intent(context, NotificationService.class);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                context.startForegroundService(new Intent(context, NotificationService.class));
            else
                context.startService(notificationIntent);
        }
    }
}
