package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.Activities.FormUtilities.CreateAccountFormUtils;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observable;
import java.util.Observer;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener, Observer, UIErrorHandler {

    private Button btnCreate;

    private EditText editUserName;
    private EditText editpassword;
    private EditText editfirstName;
    private EditText editlastName;
    private EditText editEmail;
    private EditText editphoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_screen);
        App.getModel().addObserver(this);

        btnCreate = findViewById(R.id.createAccountButton);
        btnCreate.setOnClickListener(this);

        editUserName = findViewById(R.id.createAccountUsername);
        editpassword = findViewById(R.id.createAccountPassword);
        editfirstName = findViewById(R.id.createAccountFirstName);
        editlastName = findViewById(R.id.createAccountLastName);
        editEmail = findViewById(R.id.createAccountEmail);
        editphoneNumber = findViewById(R.id.createAccountPhoneNumber);

    }

    @Override
    public void onClick(View v) {
        if(CreateAccountFormUtils.validateForm(
                editUserName,
                editpassword,
                editfirstName,
                editlastName,
                editEmail,
                editphoneNumber)){

            String userName = editUserName.getText().toString().trim();
            String password = editpassword.getText().toString().trim();
            String firstName = editfirstName.getText().toString().trim();
            String lastName = editlastName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String phoneNumber = editphoneNumber.getText().toString().trim();
            // TODO: MAKE NOT CONSTANT  *UI Team*
            User.TYPE type = User.TYPE.RIDER;

            try {
                App.getController().createNewUser(
                        userName,
                        password,
                        firstName,
                        lastName,
                        email,
                        phoneNumber,
                        type,
                        this);
            } catch (Exception e) {
                Toast.makeText(this,
                        "Failed to create user. Try again and if" +
                        "the problem persists close and restart the app.", Toast.LENGTH_LONG)
                        .show();
            }

        }
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
        if (m.getSessionUser() != null) {
            // TODO: Start MapActivity, Remove loginActivity from view stack
            Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onError(Error e) {
        String msg = e.getMessage();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
