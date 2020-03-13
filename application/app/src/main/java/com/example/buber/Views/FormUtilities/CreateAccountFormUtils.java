package com.example.buber.Views.FormUtilities;

import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountFormUtils {

    /**
     * This function is used to validate that user input when an account is created.
     * It checks that Username, password, first/last name, email, and phone number is correct
     * and non-empty
     * @param editUserName,editPassword,editFirstName,editLastName,editEmail,editPhoneNumber are the
     *      EditText view elements containing their corresponding user input (Username, First/Last name,
     *      Password, email, and phonenumber*/
    public static boolean validateForm(EditText editUserName,
                                       EditText editPassword,
                                       EditText editFirstName,
                                       EditText editLastName,
                                       EditText editEmail,
                                       EditText editPhoneNumber) {

        String userName = editUserName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();

        String userNameRegex = "([A-Za-z0-9]*)";
        String phoneNumberRegex = "[0-9-]*";
        // Referece: https://howtodoinjava.com/regex/java-regex-validate-email-address/
        // Firebase enforces strict email formatting
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

        Pattern userNamePattern = Pattern.compile(userNameRegex);
        Pattern phoneNumberPattern = Pattern.compile(phoneNumberRegex);
        Pattern emailPattern = Pattern.compile(emailRegex);


        // USERNAME
        if (userName.isEmpty()) {
            editUserName.setError("Please provide a username");
            return false;
        } else {
            editUserName.setError(null);
        }
        Matcher userNameMatcher = userNamePattern.matcher(userName);
        if (!userNameMatcher.matches()) {
            editUserName.setError("Invalid username");
            return false;
        } else {
            editUserName.setError(null);
        }
        // TODO: check if username is already taken

        // PASSWORD
        // Firebase enforces this password length
        if (password.isEmpty() || password.length() < 6) {
            editPassword.setError("Password must be at least 6 digits");
            return false;
        } else {
            editPassword.setError(null);
        }

        // FIRSTNAME
        if (firstName.isEmpty()) {
            editFirstName.setError("Please provide your first name");
            return false;
        } else {
            editFirstName.setError(null);
        }

        // LASTNAME
        if (lastName.isEmpty()) {
            editLastName.setError("Please provide your last name");
            return false;
        } else {
            editLastName.setError(null);
        }

        // EMAIL
        if (email.isEmpty()) {
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

        // PHONENUMBER
        if (phoneNumber.isEmpty()) {
            editPhoneNumber.setError("Please provide a phone number");
            return false;
        } else {
            editPhoneNumber.setError(null);
        }
        Matcher phoneNumberMatcher = phoneNumberPattern.matcher(phoneNumber);
        if (!phoneNumberMatcher.matches()) {
            editPhoneNumber.setError("Please provide a valid phone number");
            return false;
        } else {
            editPhoneNumber.setError(null);
        }

        return true;
    }
    /**
     * Function is use to validate user info when a form is edited. It checks to ensure edited
     * fields are non-empty and formatted correctly
     * @param editUserName,editFirstName,editLastName,editPhoneNumber are the EditText view elements
     *      containing user information (Username, First/Last name, Phone Number*/
    public static boolean validateEditForm(EditText editUserName,
        EditText editFirstName,
        EditText editLastName,
        EditText editPhoneNumber) {
            String userName = editUserName.getText().toString().trim();
            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String phoneNumber = editPhoneNumber.getText().toString().trim();

            String userNameRegex = "([A-Za-z0-9]*)";
            String phoneNumberRegex = "[0-9-]*";
            // Referece: https://howtodoinjava.com/regex/java-regex-validate-email-address/
            // Firebase enforces strict email formatting
            String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

            Pattern userNamePattern = Pattern.compile(userNameRegex);
            Pattern phoneNumberPattern = Pattern.compile(phoneNumberRegex);
            Pattern emailPattern = Pattern.compile(emailRegex);


            // USERNAME
            if (userName.isEmpty()) {
                editUserName.setError("Please provide a username");
                return false;
            } else {
                editUserName.setError(null);
            }
            Matcher userNameMatcher = userNamePattern.matcher(userName);
            if (!userNameMatcher.matches()) {
                editUserName.setError("Invalid username");
                return false;
            } else {
                editUserName.setError(null);
            }
            // TODO: check if username is already taken

            // FIRSTNAME
            if (firstName.isEmpty()) {
                editFirstName.setError("Please provide your first name");
                return false;
            } else {
                editFirstName.setError(null);
            }

            // LASTNAME
            if (lastName.isEmpty()) {
                editLastName.setError("Please provide your last name");
                return false;
            } else {
                editLastName.setError(null);
            }

            // PHONENUMBER
            if (phoneNumber.isEmpty()) {
                editPhoneNumber.setError("Please provide a phone number");
                return false;
            } else {
                editPhoneNumber.setError(null);
            }
            Matcher phoneNumberMatcher = phoneNumberPattern.matcher(phoneNumber);
            if (!phoneNumberMatcher.matches()) {
                editPhoneNumber.setError("Please provide a valid phone number");
                return false;
            } else {
                editPhoneNumber.setError(null);
            }
            return true;
        }
    }
