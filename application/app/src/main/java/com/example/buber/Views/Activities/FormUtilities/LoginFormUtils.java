package com.example.buber.Views.Activities.FormUtilities;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFormUtils {

    public static boolean validateForm(EditText editEmail, EditText editPassword) {

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();


        // Referece: https://howtodoinjava.com/regex/java-regex-validate-email-address/
        // Firebase enforces strict email formatting
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        // EMAIL
        if(email.isEmpty()) {
            editEmail.setError("Please provide your email");
            return false;
        } else {
            editEmail.setError(null);
        }
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) {
            editEmail.setError("Please provide a valid email");
            return false;
        } else {
            editEmail.setError(null);
        }

        // PASSWORD
        // Firebase enforces this password length
        if(password.isEmpty() || password.length() < 6) {
            editPassword.setError("Password must be at least 6 digits");
            return false;
        } else {
            editPassword.setError(null);
        }

        return true;
    }

}
