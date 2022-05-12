package edu.cuhk.csci3310.project.createRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.account.UserAccountActivity;

public class MainRequestActivity extends AppCompatActivity {

    TextView greetingTV;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_main);

        greetingTV = findViewById(R.id.greeting_TV);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        /*DatabaseReference rootReference = firebaseDatabase.getReference();
        DatabaseReference nameReference = rootReference.child("Users").child(currentUser.getUid()).child("name");
        nameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                greetingTV.setText("Hello " + snapshot.getValue().toString() + ",");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRequestActivity.this, MovingRequestActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRequestActivity.this, TutoringRequestActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRequestActivity.this, DiningRequestActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRequestActivity.this, GatheringRequestActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRequestActivity.this, BorrowingRequestActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()) {
                    case R.id.request:
                        intent = new Intent(MainRequestActivity.this, UserAccountActivity.class);
                        //startActivity(intent);
                        return true;
                    case R.id.task:
                        intent = new Intent(MainRequestActivity.this, UserAccountActivity.class);
                        //startActivity(intent);
                        return true;
                    case R.id.add:
                        return true;
                    case R.id.browse:
                        intent = new Intent(MainRequestActivity.this, UserAccountActivity.class);
                        //startActivity(intent);
                        return true;
                    case R.id.profile:
                        intent = new Intent(MainRequestActivity.this, UserAccountActivity.class);
                        startActivity(intent);
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
        bottomNavigation.setSelectedItemId(R.id.add);
    }
}