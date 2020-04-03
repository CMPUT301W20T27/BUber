package com.example.buber.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

/**
 * Login Activity, handles all existing user authentication
 */
public class LoginActivity extends AppCompatActivity implements Observer, UIErrorHandler {
    private String TAG = "LoginActivity";
    private EditText editEmail;
    private EditText editPassword;
    private CircularProgressButton driverLoginBtn;
    private CircularProgressButton riderLoginBtn;
    private int driverLoginBtnID;

    /**onCreate method creates the LoginActivity view when called
     * @param savedInstanceState is a previous saved state instance if applicable*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        App.getModel().addObserver(this);

        editEmail = findViewById(R.id.loginEmailEditText);
        editPassword = findViewById(R.id.loginPasswordEditText);
        driverLoginBtn = findViewById(R.id.loginDriverButton);
        riderLoginBtn = findViewById(R.id.loginRiderButton);
        riderLoginBtn.revertAnimation();
        driverLoginBtn.revertAnimation();
        driverLoginBtnID = R.id.loginDriverButton;
    }

    /**Handles user interaction with login button (as either driver or rider - chosen by user)
     * @param view is an instance of the view*/
    public void handleLoginClick(View view) {
        switch (view.getId()) {
            case R.id.loginRiderButton:
                riderLoginBtn.startAnimation();
                driverLoginBtn.setEnabled(false);
                break;
            case R.id.loginDriverButton:
                driverLoginBtn.startAnimation();
                riderLoginBtn.setEnabled(false);
                break;
        }

        if (LoginFormUtils.validateForm(editEmail, editPassword)) {
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            User.TYPE loginType = view.getId() == driverLoginBtnID ? User.TYPE.DRIVER : User.TYPE.RIDER;
            Intent i = new Intent(LoginActivity.this, MapActivity.class);
            App.getController().login(email, password, loginType, this, i);
        } else {
            driverLoginBtn.setEnabled(true);
            riderLoginBtn.setEnabled(true);
            riderLoginBtn.revertAnimation();
            driverLoginBtn.revertAnimation();
        }
    }

    /**Handles user interaction when create account button is clicked.
     * moves user to CreateAccountActivity
     * @param view is an instance of the view*/
    public void handleAccountCreationClick(View view) {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        this.finish();
    }

    /**update method updates the activity
     * @param o,arg are the Observable and Object to be updated*/
    @Override
    public void update(Observable o, Object arg) {

    }

    /**onDestroy method destructs LoginActivity when activity is shut down*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    /**onStart starts activity on app startup if applicable*/
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**onError handles UI errors when they come in
     * @param e is the incoming error*/
    @Override
    public void onError(Error e) {
        String msg = e.getMessage();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        driverLoginBtn.setEnabled(true);
        riderLoginBtn.setEnabled(true);
        riderLoginBtn.revertAnimation();
        driverLoginBtn.revertAnimation();
    }
}
