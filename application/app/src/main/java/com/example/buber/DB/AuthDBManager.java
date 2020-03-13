package com.example.buber.DB;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;

/**
 * Used to handle the Firebase side authentication. Wraps the FirebaseAuth object and exposes
 * methods to perform Firebase log in / log out and user creation.
 */
public class AuthDBManager {

    private static final String TAG = "In Database Manager";

    private FirebaseAuth mAuth;

    public AuthDBManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * This method queries for a user's email/password match already in Fb
     *
     * @param email
     * @param password Current User's entered email and password
     */
    public void signIn(String email, String password, EventCompletionListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    FirebaseUser fbUser = authResult.getUser();
                    HashMap<String, String> toReturn = new HashMap<>();
                    toReturn.put("doc-id", fbUser.getUid());
                    listener.onCompletion(toReturn, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, "Sign In Failed", e);
                    listener.onCompletion(null, new Error(e.getMessage()));
                });
    }

    /**
     * This method signs out the user
     */
    public void signOut() {
        mAuth.signOut();
    }

    /**
     * This method creates a new user with the entered email, password
     * @param String email
     * @param password New User's entered email and password
     * @param EventCompletionListener listener
     */
    public void createFirebaseUser(String email, String password, EventCompletionListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    Log.d(TAG, "Firebase User Created ");
                    HashMap<String, String> toReturn = new HashMap<>();
                    toReturn.put("doc-id", authResult.getUser().getUid());
                    listener.onCompletion(toReturn, null);

                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    listener.onCompletion(null, new Error(e.getMessage()));
                });
    }

    /**Returns true if a user is currently logged in*/
    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void getCurrentSessionUser(EventCompletionListener listener) {
        if (isLoggedIn()) {
            String uid = mAuth.getUid();  //looks like rider + driver have same uid
            Log.d("UID",uid);

            // TODO: Differentiate between Rider/Driver
            App.getDbManager().getDriver(uid, ((resultData, err) -> {
                if (resultData != null) {
                    listener.onCompletion(resultData, null);
                } else if (err != null){
                    listener.onCompletion(null, new Error("Failed to get current session user"));
                }
            }));
        }
    }

    public String getCurrentUserID() {
        return mAuth.getUid();
    }

}
