package edu.cuhk.csci3310.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import edu.cuhk.csci3310.project.settings.UserSettings;

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

        if (isUserEmailVerified(mFirebaseUser)){
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
        if (isUserEmailVerified(mFirebaseUser)){
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
                        if (isUserEmailVerified(mFirebaseUser)){
                            Intent CentralHubActivity = new Intent(MainActivity.this, CentralHubActivity.class);
                            startActivity(CentralHubActivity);
                        }else {
                            errorMessageTV.setText("The user e-mail is not verified.");
                            errorMessageTV.setVisibility(View.VISIBLE);

                            new android.app.AlertDialog.Builder(errorMessageTV.getContext())
                            .setTitle("Resend verfication e-mail?")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    sendVerificationEmail(mFirebaseUser, errorMessageTV.getContext());
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();

                        }
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

    public static boolean isUserEmailVerified(FirebaseUser user){
        return user != null && user.isEmailVerified();
    }

    public static void sendVerificationEmail(FirebaseUser user, Context context){
        // https://stackoverflow.com/questions/40404567/how-to-send-verification-email-with-firebase
        if (!isUserEmailVerified(user)){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(context, "A verification e-mail has been sent", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Fail to send verification e-mail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}