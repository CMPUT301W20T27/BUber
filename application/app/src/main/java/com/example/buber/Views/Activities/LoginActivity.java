package com.example.buber.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.FormUtilities.LoginFormUtils;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observable;
import java.util.Observer;

/**
 * Login Activity, handles all existing user authentication
 */
public class LoginActivity extends AppCompatActivity implements Observer, UIErrorHandler {
    private String TAG = "LoginActivity";
    private EditText editEmail;
    private EditText editPassword;
    private Button logoutButton;
    private Button driverLoginBtn;
    private Button riderLoginBtn;
    private int driverLoginBtnID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        App.getModel().addObserver(this);

        editEmail = findViewById(R.id.loginEmailEditText);
        editPassword = findViewById(R.id.loginPasswordEditText);
        driverLoginBtn = findViewById(R.id.loginDriverButton);
        riderLoginBtn = findViewById(R.id.loginRiderButton);
        driverLoginBtnID = R.id.loginDriverButton;
    }

    public void handleLoginClick(View view) {
        driverLoginBtn.setEnabled(false);
        riderLoginBtn.setEnabled(false);
        if (LoginFormUtils.validateForm(editEmail, editPassword)) {
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            User.TYPE loginType = view.getId() == driverLoginBtnID ? User.TYPE.DRIVER : User.TYPE.RIDER;
            Intent i = new Intent(LoginActivity.this, MapActivity.class);
            App.getController().login(email, password, loginType, this, i);
        } else {
            driverLoginBtn.setEnabled(true);
            riderLoginBtn.setEnabled(true);
        }
    }

    public void handleAccountCreationClick(View view) {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }


    @Override
    public void update(Observable o, Object arg) {

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
        driverLoginBtn.setEnabled(true);
        riderLoginBtn.setEnabled(true);
    }
}
