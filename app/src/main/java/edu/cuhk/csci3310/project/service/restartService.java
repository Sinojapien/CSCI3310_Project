package edu.cuhk.csci3310.project.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class restartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, NotificationService.class));
    }
}
