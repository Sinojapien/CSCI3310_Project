package edu.cuhk.csci3310.project.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.cuhk.csci3310.project.service.NotificationService;

public class UserSettings {

    private boolean mAllowNotification = false;
    private boolean mAllowOnBoot = false;

    private static FirebaseAuth mFirebaseAuth;

    public static final String TAG = "edu.cuhk.csci3310.project.settings";
    public static final String NOTIFICATION_TAG = "notification";
    public static final String ON_BOOT_TAG = "on_boot";

    public static void overrideSettings(Context context, String key, boolean value){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(getUserTag(key), value);
        editor.apply();

        update(context, settings);
    }

    public static void overrideSettings(Context context, UserSettings options){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(getUserTag(NOTIFICATION_TAG), options.mAllowNotification);
        editor.putBoolean(getUserTag(ON_BOOT_TAG), options.mAllowOnBoot);
        editor.apply();

        update(context, settings);
    }

    public static UserSettings loadSettings(Context context){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        UserSettings options = new UserSettings();
        options.mAllowNotification = settings.getBoolean(getUserTag(NOTIFICATION_TAG), false);
        options.mAllowOnBoot = settings.getBoolean(getUserTag(ON_BOOT_TAG), false);
        return options;
    }

    public static boolean getSettingNotification(Context context){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        return settings.getBoolean(getUserTag(NOTIFICATION_TAG), false);
    }

    public static boolean getSettingOnBoot(Context context){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        return settings.getBoolean(getUserTag(ON_BOOT_TAG), false);
    }

    public static void resetSettings(Context context){
        SharedPreferences settings = context.getSharedPreferences(TAG, 0);
        settings.edit().clear().commit();
        update(context, settings);
    }

    private static void update(Context context, SharedPreferences settings){
        if (settings.getBoolean(getUserTag(NOTIFICATION_TAG), false))
            NotificationService.startService(context);
        else
            context.stopService(new Intent(context, NotificationService.class));
    }

    private static String getUserTag(String tag){
        if (mFirebaseAuth == null)
            mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mFirebaseAuth.getCurrentUser();
        if (mUser != null)
            return tag + "." + mUser.getUid();

        return null;
    }

}
