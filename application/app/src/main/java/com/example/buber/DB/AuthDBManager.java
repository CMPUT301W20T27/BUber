package com.example.buber.DB;
import android.util.Log;
import androidx.annotation.NonNull;
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


public class AuthDBManager {

    private static final String TAG = "In Database Manager";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore mDatabase;

    public AuthDBManager() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseFirestore.getInstance();
    }

    /**
     * This method queries for a user's email/password match already in Fb
     *
     * @param email
     * @param password Current User's entered email and password
     */
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    Log.d(TAG, "Sign In Worked");
                    currentUser = mAuth.getCurrentUser();

                    DocumentReference docRef = mDatabase.collection("Rider").document(getcurrentUserDocID());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                } else {

                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
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
     * @param user
     * @param password New User's entered email and password
     */
    //Note to self: this "createUserWithEmailAndPassword(email, password)" query takes time so on success will take time
    public void createAccount(User user, String password, OnUserCreatedListener listener) {
        createFirebaseUser(user, password, listener);
    }

    private void createFirebaseUser(User user, String password, OnUserCreatedListener listener) {
        mAuth.createUserWithEmailAndPassword(user.getAccount().getEmail(), password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    Log.d(TAG, "Firebase User Created ");
                    String uid = mAuth.getCurrentUser().getUid();

                    addUserToDBCollection(user, uid, listener);

                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, "Create Account Failed", e);
                });

    }

    private void addUserToDBCollection(User user, String uid, OnUserCreatedListener listener) {

        mDatabase.collection(user.getType().toString())
                .document(uid)
                .set(user)
                .addOnSuccessListener((Void aVoid) -> {
                    getUserFromDB(user, uid, listener);

                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    listener.onUserCreated(null);
                    Log.d(TAG, "Failed to add user to database collection", e);
                });
    }

    private void getUserFromDB(User user, String uid, OnUserCreatedListener listener) {

        mDatabase.collection(user.getType().toString())
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (user.getType() == User.TYPE.Riders) {
                            User user = documentSnapshot.toObject(Rider.class);
                        } else {
                            User user = documentSnapshot.toObject(Driver.class);
                        }

                        listener.onUserCreated(user);
                    }
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    listener.onUserCreated(null);
                    Log.d(TAG, "Failed to get user back from database collection", e);
                });
    }

    /**
     * I used this for testing, and you are able to get things like  getcurrentUser().getEmail() ect..
     */
    public FirebaseUser getcurrentUser() {
        return mAuth.getCurrentUser();
    }

    public String getcurrentUserDocID() {
        return mAuth.getCurrentUser().getUid();
    }

    public void profileChange() {
        //TODO: be able to change username? maybe
    }
}
