package com.example.buber.Views.Activities;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginAuth{

    private static final String TAG = "In Database Manager";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public LoginAuth(){
        mAuth = FirebaseAuth.getInstance();          //Initialize FirebaseAuth
        currentUser = mAuth.getCurrentUser();
    }

    /**
     * This method queries for a user's email/password match already in Fb
     * @param  email
     * @param password
     * Current User's entered email and password
     */
    public void signIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email,  password)
                .addOnSuccessListener((AuthResult authResult) -> {

                    Log.d(TAG, "Sign In Worked");
                    currentUser = mAuth.getCurrentUser();

                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, "Sign In Failed", e);
                });
    }

    /**
     * This method signs out the user
     */
    public void signOut() {
        mAuth.signOut();
        currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "Signed out");

    }

    /**
     * This method creates a new user with the entered email, password
     * @param  email
     * @param password
     * New User's entered email and password
     */
    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> {

                    currentUser = mAuth.getCurrentUser();
                    Log.d(TAG, "User Created ");

                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, "Create Account Failed", e);
                });
    }
    /**
     * I used this for testing, and you are able to get things like  getcurrentUser().getEmail() ect..
     */
    public FirebaseUser getcurrentUser(){
        return  mAuth.getCurrentUser();
    }
    /**
     * This method is specifically  for entering the UserID's into rider or driver class
     */
    public String getcurrentUserID(){
        return   mAuth.getCurrentUser().getUid();
    }

    public void profileChange(){
    //TODO: be able to change username? maybe
    }
}
