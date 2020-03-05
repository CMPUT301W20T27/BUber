package com.example.buber.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.Activities.FormUtilities.LoginFormUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements Observer {
    private String TAG = "LoginActivity";
    private EditText editEmail;
    private EditText editPassword;
    private int driverLoginBtnID;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        App.getModel().addObserver(this);

        editEmail = findViewById(R.id.loginEmailEditText);
        editPassword = findViewById(R.id.loginPasswordEditText);
        driverLoginBtnID = R.id.loginDriverButton;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are STILL logged in", Toast.LENGTH_SHORT).show();
                    // move to the next intent
                } else {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void handleLoginClick(View view) {
        if (LoginFormUtils.validateForm(editEmail, editPassword)) {
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            User.TYPE loginType = view.getId() == driverLoginBtnID ? User.TYPE.Drivers : User.TYPE.Riders;

            mFirebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    if (e.getErrorCode().equals(LoginFormUtils.FirebaseAuthPassWordExceptionCode)) {
                                        editPassword.setError("Invalid Password");
                                        editEmail.requestFocus();
                                    }
                                } catch (Exception e) {
                                    if (e.getMessage().equals(LoginFormUtils.FirebaseAuthEmailExceptionMSG)) {
                                        editEmail.setError("Unrecognized Email");
                                        editEmail.requestFocus();
                                    }
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "You are NOW logged in.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MapActivity.class));
                            }
                        }
                    });
        }
    }

    public void handleAccountCreationClick(View view) {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }


    @Override
    public void update(Observable o, Object arg) {
        ApplicationModel m = (ApplicationModel) o;
        System.out.println("UPDATE CALLED");
        if (m.getSessionUser() != null) {
            startActivity(new Intent(LoginActivity.this, MapActivity.class));
            this.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
