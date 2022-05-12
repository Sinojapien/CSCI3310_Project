package edu.cuhk.csci3310.project.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.createRequest.MainRequestActivity;

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
                ((Activity) context).finish();
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