package edu.cuhk.csci3310.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.cuhk.csci3310.project.account.SignupActivity;
import edu.cuhk.csci3310.project.account.UserAccountActivity;
import edu.cuhk.csci3310.project.account.ValidateInput;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailET, passwordET;
    private Button loginBTN;
    private TextView signupTV;
    private ProgressBar progressBar;
    private TextView errorMessageTV;

    ValidateInput validateInput;

    String email, password;

    private static final String TAG = "MainActivity";

    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [Start] Login code
        validateInput = new ValidateInput(this);

        emailET = findViewById(R.id.email_ET);
        passwordET = findViewById(R.id.password_ET);

        loginBTN = findViewById(R.id.login_btn);
        signupTV = findViewById(R.id.signup_TV);
        loginBTN.setOnClickListener(this);
        signupTV.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        errorMessageTV = findViewById(R.id.error_message_TV);
        // [Stop] Login code

        // Maintaining Login, would not be called upon back button navigation
        mFirebaseUser = mAuth.getCurrentUser();

        if (mFirebaseUser != null){
            emailET.setText(mFirebaseUser.getEmail());

            Intent CentralHubActivity = new Intent(MainActivity.this, CentralHubActivity.class);
            startActivity(CentralHubActivity);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id) {
            case R.id.login_btn:
                handleLoginBtnClick();
                break;
            case R.id.signup_TV:
                handleSignUpClick();
                break;
        }
    }

    private void handleLoginBtnClick() {
        showProgressBar();
        errorMessageTV.setVisibility(View.INVISIBLE);
        // Maintaining Login
        if (mFirebaseUser != null){
            Intent CentralHubActivity = new Intent(MainActivity.this, CentralHubActivity.class);
            startActivity(CentralHubActivity);
            return;
        }

        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        if(validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        hideProgressBar();
                        Intent CentralHubActivity = new Intent(MainActivity.this, CentralHubActivity.class);
                        startActivity(CentralHubActivity);
                    } else {
                        hideProgressBar();
                        errorMessageTV.setText(task.getException().getMessage());
                        errorMessageTV.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void handleSignUpClick() {
        Intent SignupActivity = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(SignupActivity);
    }

}