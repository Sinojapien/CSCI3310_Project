package edu.cuhk.csci3310.project.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cuhk.csci3310.project.MainActivity;
import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.createRequest.MainRequestActivity;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.searchRequest.RequestHistoryActivity;
import edu.cuhk.csci3310.project.service.NotificationService;
import edu.cuhk.csci3310.project.settings.UserSettings;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    TextView usernameTV;
    TextView emailTV;
    Button logoutBtn;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Context context;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        logoutBtn = findViewById(R.id.logout_btn);
        usernameTV = findViewById(R.id.username_TV);
        emailTV = findViewById(R.id.email_TV);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String email = user.getEmail();
        emailTV.setText(email);

        context = this;

        logoutBtn.setOnClickListener(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootReference = firebaseDatabase.getReference();
        DatabaseReference nameReference = rootReference.child("Users").child(user.getUid()).child("name");
        nameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usernameTV.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.request:
                        intent = new Intent(UserAccountActivity.this, UserAccountActivity.class);
                        //startActivity(intent);
                        return true;
                    case R.id.task:
                        intent = new Intent(UserAccountActivity.this, UserAccountActivity.class);
                        //startActivity(intent);
                        return true;
                    case R.id.add:
                        intent = new Intent(UserAccountActivity.this, MainRequestActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.browse:
                        intent = new Intent(UserAccountActivity.this, UserAccountActivity.class);
                        //startActivity(intent);
                        return true;
                    case R.id.profile:
                        return true;
                    default:
                        return false;
                }
            }
        });

        Switch notificationOptionSwitch = findViewById(R.id.notification_layout).findViewById(R.id.notification_switch);
        Switch onBootOptionSwitch = findViewById(R.id.on_boot_layout).findViewById(R.id.on_boot_switch);

        notificationOptionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSettings.overrideSettings(context, UserSettings.NOTIFICATION_TAG, ((Switch) view).isChecked());
            }
        });

        onBootOptionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSettings.overrideSettings(view.getContext(), UserSettings.ON_BOOT_TAG, ((Switch) view).isChecked());
            }
        });

        findViewById(R.id.reset_option_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
                builder.setTitle("Reset all user preferences?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notificationOptionSwitch.setChecked(false);
                        onBootOptionSwitch.setChecked(false);
                        UserSettings.resetSettings(view.getContext());
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });

        notificationOptionSwitch.setChecked(UserSettings.getSettingNotification(this));
        onBootOptionSwitch.setChecked(UserSettings.getSettingOnBoot(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.profile);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout_btn:
                showLogoutDialog();
                break;
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Are you sure you want to log out?").setPositiveButton("Yes, logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                //((Activity) context).finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}