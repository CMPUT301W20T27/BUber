package com.example.buber.DB;import android.util.Log;
import androidx.annotation.NonNull;
import com.example.buber.Services.ApplicationServiceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
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

    public AuthDBManager(){
        mAuth = FirebaseAuth.getInstance();          //Initialize FirebaseAuth
        currentUser = mAuth.getCurrentUser();
    }

    /**
     * This method queries for a user's email/password match already in Fb
     * @param  email
     * @param password
     * Current User's entered email and password
     */
    public void signIn(String email, String password,ApplicationServiceHelper x){

        mAuth.signInWithEmailAndPassword(email,  password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    Log.d(TAG, "Sign In Worked");
                    currentUser = mAuth.getCurrentUser();


                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("Rider").document(getcurrentUserDocID());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    x.aftersuccessfulLoginofrider(getcurrentUserDocID());
                                } else {
                                    x.aftersuccessfulLoginofdriver(getcurrentUserDocID());
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
     * @param  email
     * @param password
     * New User's entered email and password
     */
    //Note to self: this "createUserWithEmailAndPassword(email, password)" query takes time so on success will take time
    public void createAccount(String email, String password, ApplicationServiceHelper x){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    currentUser = mAuth.getCurrentUser();
                    Log.d(TAG, "User Created ");

                    x.aftersuccessfulCreataAccount(currentUser.getUid());

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
     * This method is specifically  for entering the DocID's into rider or driver DocID
     */
    public String getcurrentUserDocID(){
        return   mAuth.getCurrentUser().getUid();
    }

    public void profileChange(){
        //TODO: be able to change username? maybe
    }
}
