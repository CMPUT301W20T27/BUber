package com.example.buber.Views.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Observable;


import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO:database connections, model, etc..
        super.onCreate(savedInstanceState);
        ApplicationModel m = App.getModel();
        m.addObserver(this);
        handleLoginStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        ApplicationModel m = (ApplicationModel) o;
    }

    private void handleLoginStatus() {
        if(App.getAuthDBManager().isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, MapActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

}
