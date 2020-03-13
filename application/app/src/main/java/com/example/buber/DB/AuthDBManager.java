package com.example.buber.DB;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Driver;
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
     * Queries for a user's email/password match already in Fb. On success the listener returns the
     * document id of the user that logged in. On failure the listener returns the exception
     * @param email that the user enters
     * @param password Current User's entered email and password
     * @returns listener on successful/failed document query.
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
     * Signs out the user
     */
    public void signOut() {
        mAuth.signOut();
    }

    /**
     * Creates a new user with the entered email and password. On success the listener returns the
     * document id of the user that logged in. On failure the listener returns the exception
     * @param email that the user enters
     * @param password New User's entered email and password
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     * @returns listener on successful/failed document creation in Firebase.
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

    /**
    *@Returns true if a user is currently logged in
    */
    public boolean isLoggedIn() {

        return mAuth.getCurrentUser() != null;
    }

    /**
     * Retrieves the session user object from the Firebase collection if the user is currently logged in
     * On success the lister returns a driver object or a rider object. On failure the listener returns the exception
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     * @returns listener on successful/failed document creation in Firebase.
     */
    public void getCurrentSessionUser(EventCompletionListener listener) {
        if (isLoggedIn()) {
            String uid = App.getAuthDBManager().getCurrentUserID();
            Log.d("UID",uid);

            App.getDbManager().getDriver(uid, ((resultData, err) -> {
                if (err != null) {
                    listener.onCompletion(null, err);
                } else {
                    Driver driverProfile = (Driver) resultData.get("user");
                    if (driverProfile.getDriverLoggedOn()) {
                        listener.onCompletion(resultData, null);
                    } else {
                        // Get the rider
                        App.getDbManager().getRider(uid, (resultData1, err1) -> {
                            if (err != null) listener.onCompletion(null, err);
                            else {
                                listener.onCompletion(resultData1, null);
                            }
                        });

                    }
                }
            }));
        }
    }

    /**
     * @returns the current users Firebase document id
     */
    public String getCurrentUserID() {

        return mAuth.getUid();
    }

}
