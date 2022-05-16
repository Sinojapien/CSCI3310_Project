package edu.cuhk.csci3310.project.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import edu.cuhk.csci3310.project.CentralHubActivity;
import edu.cuhk.csci3310.project.MainActivity;
import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.viewModel.RequestHistoryViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

// https://www.tutorialspoint.com/send-a-notification-when-the-android-app-is-closed

// https://stackoverflow.com/questions/52308648/android-firebase-push-notification-click-event
// android studio send notification upon firebase event
// https://firebase.google.com/docs/database/admin/retrieve-data?hl=en

// Service
// https://developer.android.com/guide/components/foreground-services
// https://stackoverflow.com/questions/50481821/using-a-listener-after-an-android-application-is-closed
// https://stackoverflow.com/questions/29323317/how-to-stop-android-service-when-app-is-closed

// https://developer.android.com/about/versions/oreo/background
// Auto-start
// https://stackoverflow.com/questions/7690350/android-start-service-on-boot
// Running multiple ones?
// https://stackoverflow.com/questions/8019899/what-happens-if-a-android-service-is-started-multiple-times
// https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android

// Notification
// https://developer.android.com/training/notify-user/build-notification#java
// https://stackoverflow.com/questions/39674850/send-a-notification-when-the-app-is-closed
// https://www.tutorialspoint.com/send-a-notification-when-the-android-app-is-closed

// Firebase
// https://stackoverflow.com/questions/62303395/how-to-listen-to-changes-in-firebase-database-collection
// https://stackoverflow.com/questions/50481821/using-a-listener-after-an-android-application-is-closed
// Maintaining Login State
// https://stackoverflow.com/questions/56163255/stay-logged-in-with-firebase-to-the-app-when-closed

// Developer Options
// https://www.techrepublic.com/article/how-to-view-all-running-services-on-android-11/

public class NotificationService extends IntentService {

    private class FirebaseQueryListener implements EventListener<QuerySnapshot>{

        public FirebaseQueryListener(){}

        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            // Handle errors
            if (error != null) {
                createNotification("Error: fail to maintain connection with database");
                if (mEnquireRegistration != null) {
                    mEnquireRegistration.remove();
                    mEnquireRegistration = null;
                }
                if (mAcceptRegistration != null) {
                    mAcceptRegistration.remove();
                    mAcceptRegistration = null;
                }
                return;
            }

            for (DocumentChange change : value.getDocumentChanges()) {
                switch (change.getType()) {
                    // https://stackoverflow.com/questions/52295327/how-to-get-the-modified-field-or-data-from-the-doc-firebase-firestore-realtime-u
                    // No field level access
                    case ADDED:
//                        Favor favor = change.getDocument().toObject(Favor.class);
                        createNotification(mFirebaseAuth.getCurrentUser().getDisplayName() + ", there are new request(s)!");
                        break;
                    case MODIFIED:
                        createNotification(mFirebaseAuth.getCurrentUser().getDisplayName() + ", there are changes in your request status!");
                        break;
                    case REMOVED:
                        createNotification(mFirebaseAuth.getCurrentUser().getDisplayName() + ", one or more of your request are removed.");
                        break;
                }
            }

        }
    }

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;
    private Query mEnquireQuery;
    private Query mAcceptQuery;
    private ListenerRegistration mEnquireRegistration;
    private ListenerRegistration mAcceptRegistration;

    private static final String TAG = "NotificationService";
    public static final String TAG_EMAIL = "NotificationService.email";
    public static final String TAG_PASSWORD = "NotificationService.password";
    public static final String CHANNEL_ID = "10001" ;

    public NotificationService() {
        super(NotificationService.class.getName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if (user != null){
            Query query = mFirestore.collection("favors").limit(50);
            mEnquireQuery = query.whereEqualTo("enquirer", user.getUid());
            mAcceptQuery = query.whereEqualTo("accepter", user.getUid());
            mEnquireRegistration = mEnquireQuery.addSnapshotListener(new FirebaseQueryListener());
            mAcceptRegistration = mAcceptQuery.addSnapshotListener(new FirebaseQueryListener());

        }else{
            createNotification("User have signed out.");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        // receive null intent upon restart
//
////        String email;
////        String password;
////
////        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
////            @Override
////            public void onComplete(@NonNull Task<AuthResult> task) {
////                if(task.isSuccessful()) {
////
////                }
////            }
////        });
//
//        FirebaseUser user = mFirebaseAuth.getCurrentUser();
//
//        if (user != null){
//            Query query = mFirestore.collection("favors").limit(50);
//            mEnquireQuery = query.whereEqualTo("enquirer", user.getUid());
//            mAcceptQuery = query.whereEqualTo("accepter", user.getUid());
//            mEnquireRegistration = mEnquireQuery.addSnapshotListener(new FirebaseQueryListener());
//            mAcceptRegistration = mAcceptQuery.addSnapshotListener(new FirebaseQueryListener());
//
//        }else{
//            createNotification("User have signed out.");
//        }
//
//        return START_STICKY;
//    }

//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        super.onTaskRemoved(rootIntent);
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }

    protected void createNotification(String contentText){
        if (isAppRunning(getApplicationContext())) return;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("CUHKFavor: ")
                .setContentText(contentText)
                .setTicker(contentText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(""))
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CUHKFavor";
            String description = "It is used to update user upon changes of favors in Firebase store.";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            //NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    public static boolean isAppRunning(Context context) {
        // https://stackoverflow.com/questions/55742122/how-to-stop-notification-when-app-is-running

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.uid == context.getApplicationInfo().uid) {
                return true;
            }
        }

        return false;
    }

    public static boolean isRunning(Context context) {
        // https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android

        ActivityManager serviceManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : serviceManager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}