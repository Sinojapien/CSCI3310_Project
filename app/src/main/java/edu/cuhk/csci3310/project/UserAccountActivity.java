package edu.cuhk.csci3310.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    TextView usernameTV;
    TextView emailTV;
    Button logoutBtn;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        logoutBtn = findViewById(R.id.logout_btn);
        usernameTV = findViewById(R.id.username_TV);
        emailTV = findViewById(R.id.email_TV);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        emailTV.setText(user.getEmail());

        context = this;

        logoutBtn.setOnClickListener(this);
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