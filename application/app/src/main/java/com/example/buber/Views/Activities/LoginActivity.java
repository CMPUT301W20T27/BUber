package com.example.buber.Views.Activities;

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

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements  Observer {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private int driverLoginButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        App.getModel().addObserver(this);

        usernameEditText = findViewById(R.id.loginUsernameEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        driverLoginButtonId = R.id.loginDriverButton;
    }

    public void handleLoginClick(View view) {
        ApplicationController c = App.getController();

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        User.TYPE loginType = view.getId() == driverLoginButtonId ?  User.TYPE.DRIVER : User.TYPE.RIDER;

        c.login(username, password, loginType);
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
}
