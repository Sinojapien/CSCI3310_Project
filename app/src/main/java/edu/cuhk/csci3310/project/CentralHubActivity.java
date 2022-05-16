package edu.cuhk.csci3310.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import edu.cuhk.csci3310.project.account.UserAccountActivity; // for sending intent
import edu.cuhk.csci3310.project.createRequest.MainRequestActivity;
import edu.cuhk.csci3310.project.searchRequest.MainSearchRequestActivity;
import edu.cuhk.csci3310.project.searchRequest.RequestHistoryActivity;

/**
 * The CentralHubActivity is page shown after successful login
 */
public class CentralHubActivity extends AppCompatActivity{
    // layout page may need more stuff, e.g. photo, text
    private TextView mUserDisplayName;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.d("CentralHubActivity", "creating CentralHubActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_hub);

        firebaseAuth = FirebaseAuth.getInstance();

        // set user name, if any
        mUserDisplayName = findViewById(R.id.hub_username);
        mUserDisplayName.setText(firebaseAuth.getCurrentUser().getDisplayName());

        Button accountBtn = findViewById(R.id.hub_account_btn);
        accountBtn.setOnClickListener(view -> {
            Intent UserAccountActivity = new Intent(CentralHubActivity.this, UserAccountActivity.class);
            startActivity(UserAccountActivity);
        });
        Button newRequestBtn = findViewById(R.id.hub_new_request_btn);
        newRequestBtn.setOnClickListener(view -> {
            Intent MainRequestActivity = new Intent(CentralHubActivity.this, MainRequestActivity.class);
            startActivity(MainRequestActivity);
        });
        Button searchRequestBtn = findViewById(R.id.hub_search_request_btn);
        searchRequestBtn.setOnClickListener(view -> {
            Intent MainSearchRequestActivity = new Intent(CentralHubActivity.this, MainSearchRequestActivity.class);
            startActivity(MainSearchRequestActivity);
        });
        Button requestHistoryBtn = findViewById(R.id.request_history_btn);
        requestHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CentralHubActivity.this, RequestHistoryActivity.class);
                startActivity(intent);
            }
        });
        // Log.d("CentralHubActivity", "finish creating CentralHubActivity");
    }
}