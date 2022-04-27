package edu.cuhk.csci3310.project;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Pattern;

public class ValidateInput {

    Context context;

    public ValidateInput(Context context) {
        this.context = context;
    }

    // Method 1: validate the email
    public boolean checkIfEmailIsValid(String email) {
        if(email.length() == 0) {
            Toast.makeText(context, "Please enter your email ID!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter valid email ID!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // Method 2: validate the password
    public boolean checkIfPasswordIsValid(String password) {
        if(password.length() == 0) {
            Toast.makeText(context, "Please enter a password!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(password.length() < 6) {
            Toast.makeText(context, "Please enter a password of at least 6 character", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}

