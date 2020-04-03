package com.example.buber.Views.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.Account;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.FormUtilities.CreateAccountFormUtils;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observable;
import java.util.Observer;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

/**
 * Handles all User account updating.
 */
public class EditAccountActivity extends AppCompatActivity implements Observer, UIErrorHandler {

    private EditText editUserName;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPhoneNumber;

    private String oldUserName;
    private String oldFirstName;
    private String oldLastName;
    private String oldPhoneNumber;

    private CircularProgressButton btnSave;

    private TextWatcher txtWatcher;

    /**onCreate method creates the EditAccountActivity when it is called
     * @param savedInstanceState is a previous saved state if applicable*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        App.getModel().addObserver(this);
        editUserName = findViewById(R.id.createAccountUsername);
        editFirstName = findViewById(R.id.createAccountFirstName);
        editLastName = findViewById(R.id.createAccountLastName);
        editPhoneNumber = findViewById(R.id.createAccountPhoneNumber);
        btnSave = findViewById(R.id.buttonEditAccountSave);
        btnSave.setEnabled(false);  //until changes are made

        txtWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                btnSave.setEnabled(true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };
        fillData();
    }

    /**Fills edit text views with current user information (username, first/last name, phone number)*/
    public void fillData(){
        User curUser = App.getModel().getSessionUser();

        oldUserName = curUser.getUsername();
        oldFirstName = curUser.getAccount().getFirstName();
        oldLastName = curUser.getAccount().getLastName();
        oldPhoneNumber = curUser.getAccount().getPhoneNumber();

        editUserName.setText(oldUserName);
        editFirstName.setText(oldFirstName);
        editLastName.setText(oldLastName);
        editPhoneNumber.setText(oldPhoneNumber);

        editUserName.addTextChangedListener(txtWatcher);
        editPhoneNumber.addTextChangedListener(txtWatcher);
        editLastName.addTextChangedListener(txtWatcher);
        editFirstName.addTextChangedListener(txtWatcher);

    }

    /**Handles user interaction when save changes button is clicked in EditAccountActivity
     * @param v is the view instance*/
    public void handleEditAccountSaveClick(View v) {
        if (CreateAccountFormUtils.validateEditForm(
                editUserName,
                editFirstName,
                editLastName,
                editPhoneNumber)) {

            String newUserName = editUserName.getText().toString().trim();
            String newFirstName = editFirstName.getText().toString().trim();
            String newLastName = editLastName.getText().toString().trim();
            String newPhoneNumber = editPhoneNumber.getText().toString().trim();

            btnSave.startAnimation();
            updateNonCriticalUserFields(newUserName,newFirstName,newLastName,newPhoneNumber);
        }
        else{
            btnSave.revertAnimation();
            btnSave.setEnabled(false);
        }
    }

    /**Updates username, first name, last name, and phone number fields
     * @param newUserName is the new username input
     * @param newFirstName is the new first name input
     * @param newLastName is the new last name input
     * @param newPhoneNumber is the new phone number input*/
    /**Updates username, first name, last name, and phone number fields*/
    public void updateNonCriticalUserFields(String newUserName, String newFirstName,
                                            String newLastName,String newPhoneNumber){
        User sessionUser = App.getModel().getSessionUser();
        if (sessionUser != null) {
            sessionUser.setUsername(newUserName);
            Account sessionUserAccount = sessionUser.getAccount();
            sessionUserAccount.setFirstName(newFirstName);
            sessionUserAccount.setLastName(newLastName);
            sessionUserAccount.setPhoneNumber(newPhoneNumber);

            ApplicationController.editAccountUpdate(sessionUser, sessionUser.getType(), this);  //updates db
            App.getModel().setSessionUser(sessionUser);  //update the model
            Toast.makeText(this, "Account Updated!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            Toast.makeText(this, "Oop! Something went wrong", Toast.LENGTH_SHORT).show();
            btnSave.revertAnimation();
        }
    }

    /**update updates the EditAccountActivity when called
     * @param o,arg are the Observable and Object used in the update*/
    @Override
    public void update(Observable o, Object arg) { }

    /**onDestroy handles destruction of EditAccountActivity when activity is shut down*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    /**onError handles incoming errors if applicable*/
    @Override
    public void onError(Error e) {

    }
}
