package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.Activities.FormUtilities.LoginFormUtils;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements Observer, UIErrorHandler {
    private String TAG = "LoginActivity";
    private EditText editEmail;
    private EditText editPassword;
    private Button logoutButton;
    private int driverLoginBtnID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        App.getModel().addObserver(this);

        editEmail = findViewById(R.id.loginEmailEditText);
        editPassword = findViewById(R.id.loginPasswordEditText);
        driverLoginBtnID = R.id.loginDriverButton;
    }

    public void handleLoginClick(View view) {
        if (LoginFormUtils.validateForm(editEmail, editPassword)) {
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            User.TYPE loginType = view.getId() == driverLoginBtnID ? User.TYPE.DRIVER : User.TYPE.RIDER;
            App.getController().login(email, password, loginType, this);
        }
    }

    public void handleAccountCreationClick(View view) {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }


    @Override
    public void update(Observable o, Object arg) {
        ApplicationModel m = (ApplicationModel) o;
        if (m.getSessionUser() != null) {
            startActivity(new Intent(LoginActivity.this, MapActivity.class));
            Toast.makeText(LoginActivity.this, "You are NOW logged in.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onError(Error e) {
        String msg = e.getMessage();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
