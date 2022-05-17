package edu.cuhk.csci3310.project.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import edu.cuhk.csci3310.project.settings.UserSettings;

public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (UserSettings.getSettingOnBoot(context) && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            NotificationService.startService(context);
        }
    }
}
