package com.example.buber.DB;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.Executor;


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
     * @param user
     * @param password New User's entered email and password
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

    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void getCurrentSessionUser(EventCompletionListener listener) {
        if (isLoggedIn()) {
            String uid = mAuth.getUid();
            App.getDbManager().getRider(uid, ((resultData, err) -> {
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
