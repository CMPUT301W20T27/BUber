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
import com.example.buber.Views.Activities.FormUtilities.LoginFormUtils;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements  Observer {
    private EditText editEmail;
    private EditText editPassword;
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
        ApplicationController controller = App.getController();

        if (LoginFormUtils.validateForm(editEmail, editPassword)) {
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            User.TYPE loginType = view.getId() == driverLoginBtnID ?  User.TYPE.Drivers : User.TYPE.Riders;
            controller.login(email, password, loginType);
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
}
