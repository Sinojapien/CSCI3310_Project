package edu.cuhk.csci3310.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private EditText emailET, passwordET, passwordAgainET;
    private Button signupBTN;
    private TextView loginTV;

    ValidateInput validateInput;

    String email, password, passwordAgain;

    FirebaseAuth mAuth;

    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // [Start] Signup part
        validateInput = new ValidateInput(this);

        emailET = findViewById(R.id.email_ET);
        passwordET = findViewById(R.id.password_ET);
        passwordAgainET = findViewById(R.id.password_again_ET);
        signupBTN = findViewById(R.id.signup_btn);
        loginTV = findViewById(R.id.login_TV);

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
        // Fetching the string values
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        passwordAgain = passwordAgainET.getText().toString();

        if(validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)) {
            if(password.equals(passwordAgain)) {
                // Signup user with email and password
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignupActivity.this, "Signup is successful for " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Error occured: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Passwords don't match. Please enter again!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}