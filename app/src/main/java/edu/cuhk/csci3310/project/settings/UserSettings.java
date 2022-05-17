package edu.cuhk.csci3310.project.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import edu.cuhk.csci3310.project.service.NotificationService;

public class UserSettings {

    private boolean mAllowNotification = false;
    private boolean mAllowOnBoot = false;

    public static final String TAG = "edu.cuhk.csci3310.project.settings";
    public static final String NOTIFICATION_TAG = "notification";
    public static final String ON_BOOT_TAG = "on_boot";

    public static void overrideSettings(Context context, String key, boolean value){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();

        update(context, settings);
    }

    public static void overrideSettings(Context context, UserSettings options){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(NOTIFICATION_TAG, options.mAllowNotification);
        editor.putBoolean(ON_BOOT_TAG, options.mAllowOnBoot);
        editor.apply();

        update(context, settings);
    }

    public static UserSettings loadSettings(Context context){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        UserSettings options = new UserSettings();
        options.mAllowNotification = settings.getBoolean(NOTIFICATION_TAG, false);
        options.mAllowOnBoot = settings.getBoolean(ON_BOOT_TAG, false);
        return options;
    }

    public static boolean getSettingNotification(Context context){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        return settings.getBoolean(NOTIFICATION_TAG, false);
    }

    public static boolean getSettingOnBoot(Context context){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        return settings.getBoolean(ON_BOOT_TAG, false);
    }

    public static void resetSettings(Context context){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        settings.edit().clear().commit();
        update(context, settings);
    }

    private static void update(Context context, SharedPreferences settings){
        if (settings.getBoolean(NOTIFICATION_TAG, false))
            NotificationService.startService(context);
        else
            context.stopService(new Intent(context, NotificationService.class));
    }

}
