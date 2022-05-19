package edu.cuhk.csci3310.project.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.cuhk.csci3310.project.CentralHubActivity;
import edu.cuhk.csci3310.project.MainActivity;
import edu.cuhk.csci3310.project.R;

public class SignupActivity extends AppCompatActivity {

    private EditText emailET, nameET, passwordET, passwordAgainET;
    private Button signupBTN;
    private TextView loginTV, errorMessageTV;
    private ProgressBar progressBar;

    ValidateInput validateInput;

    String email, name, password, passwordAgain;

    FirebaseAuth mAuth;

    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // [Start] Signup part
        validateInput = new ValidateInput(this);

        emailET = findViewById(R.id.email_ET);
        nameET = findViewById(R.id.name_ET);
        passwordET = findViewById(R.id.password_ET);
        passwordAgainET = findViewById(R.id.password_again_ET);
        signupBTN = findViewById(R.id.signup_btn);
        loginTV = findViewById(R.id.login_TV);
        errorMessageTV = findViewById(R.id.error_message_TV);
        progressBar = findViewById(R.id.progressBar);

        signupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignupBtnClick();
            }
        });

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        // [Stop] Signup part
    }

    private void handleSignupBtnClick() {
        showProgressBar();
        errorMessageTV.setVisibility(View.INVISIBLE);
        // Fetching the string values
        email = emailET.getText().toString();
        name = nameET.getText().toString();
        password = passwordET.getText().toString();
        passwordAgain = passwordAgainET.getText().toString();

        // errorMessageTV.setVisibility(View.INVISIBLE);
        if(!name.isEmpty()) {
            // signup user
            if(validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)) {
                if(password.equals(passwordAgain)) {
                    // Signup user with email and password
                    Context context = SignupActivity.this;
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                hideProgressBar();
                                FirebaseUser user = mAuth.getCurrentUser();
                                saveNameInFirebaseRealtimeDatabase(user);
                                MainActivity.sendVerificationEmail(user, context);
                                Intent intent = new Intent(SignupActivity.this, edu.cuhk.csci3310.project.MainActivity.class);
                                startActivity(intent);
//                                Intent CentralHubActivity = new Intent(SignupActivity.this, edu.cuhk.csci3310.project.CentralHubActivity.class);
//                                startActivity(CentralHubActivity);
                            } else {
                                hideProgressBar();
                                errorMessageTV.setText(task.getException().getMessage());
                                errorMessageTV.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    hideProgressBar();
                    errorMessageTV.setText("Passwords don't match. Please enter again");
                    errorMessageTV.setVisibility(View.VISIBLE);
                }
            } else {
                hideProgressBar();
                errorMessageTV.setText("Please enter a valid username and password");
                errorMessageTV.setVisibility(View.VISIBLE);
            }
        } else {
            hideProgressBar();
            errorMessageTV.setText("Please enter a name!");
            errorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    private void saveNameInFirebaseRealtimeDatabase(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        db.collection("users")
                .document(user.getUid())
                .set(data);

        // also set the display name
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }



}