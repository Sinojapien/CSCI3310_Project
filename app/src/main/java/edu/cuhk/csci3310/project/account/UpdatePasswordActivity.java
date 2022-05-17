package edu.cuhk.csci3310.project.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.cuhk.csci3310.project.CentralHubActivity;
import edu.cuhk.csci3310.project.R;

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText passwordET, passwordAgainET;
    private Button updateButton;
    private ProgressBar progressBar;
    private TextView errorMessageTV;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    ValidateInput validateInput;

    String password, passwordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        passwordET = findViewById(R.id.password_ET);
        passwordAgainET = findViewById(R.id.password_again_ET);
        updateButton = findViewById(R.id.updatePassword_btn);
        progressBar = findViewById(R.id.progressBar);
        errorMessageTV = findViewById(R.id.error_message_TV);

        validateInput = new ValidateInput(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        updateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        updatePassword();
    }

    private void updatePassword() {
        showProgressBar();
        errorMessageTV.setVisibility(View.INVISIBLE);

        password = passwordET.getText().toString();
        passwordAgain = passwordAgainET.getText().toString();

        if(validateInput.checkIfPasswordIsValid(password)) {
            if(password.equals(passwordAgain)) {
                mFirebaseUser.updatePassword(password)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    hideProgressBar();
                                    Intent intent = new Intent(UpdatePasswordActivity.this, CentralHubActivity.class);
                                    startActivity(intent);
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
            errorMessageTV.setText("Please enter a valid password");
            errorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setErrorMessageTV(String message) {
        errorMessageTV.setText(message);
    }
}