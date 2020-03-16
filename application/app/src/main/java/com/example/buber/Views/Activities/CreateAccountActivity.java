package com.example.buber.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.transition.CircularPropagation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.FormUtilities.CreateAccountFormUtils;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observable;
import java.util.Observer;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

/**
 * Handles all new account activity creation. Valids the user input form and uses the application
 * controller to pass data to Firebase.
 * TODO: Allow ability to sign up and initially sign in as rider or driver.
 */
public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener, Observer, UIErrorHandler {

    private CircularProgressButton btnCreate;

    private EditText editUserName;
    private EditText editpassword;
    private EditText editfirstName;
    private EditText editlastName;
    private EditText editEmail;
    private EditText editphoneNumber;

    /**onCreate method creates the CreateAccountActivity when called
     * @param savedInstanceState is a previous saved state if applicable */
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

    /**onClick method handles user clicking create account button
     * @param v is the view instance*/
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

            btnCreate.startAnimation();
            App.getController().createNewUser(
                            userName,
                            password,
                            firstName,
                            lastName,
                            email,
                            phoneNumber,
                            type,
                            this);
            }
    }

    /**onDestroy destructs the CreateAccountActivity when it is shut down*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    /**update updates the CreateAccountActivity when called
     * @param o,arg are the Observable and Object used in the update*/
    @Override
    public void update(Observable o, Object arg) {
        ApplicationModel m = (ApplicationModel) o;
        if (m.getSessionUser() != null) {

            Intent i = new Intent(CreateAccountActivity.this, MapActivity.class);
            // LoginActivity has already been finished before going to Map
            startActivity(i);
            this.finish();
            Toast.makeText(this, "Account Created Successfully. Logged in!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**onBackPressed handles user interaction with the back button*/
    @Override
    public void onBackPressed() {
        // we need to start LoginActivity because it was finished to get here
        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
        this.finish();
    }

    /**onError handles incoming errors to CreateAccountActivity
     * @param e is the incoming error*/
    @Override
    public void onError(Error e) {
        String msg = e.getMessage();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
