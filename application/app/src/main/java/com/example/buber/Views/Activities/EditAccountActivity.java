package com.example.buber.Views.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Model.Account;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.FormUtilities.CreateAccountFormUtils;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observable;
import java.util.Observer;

public class EditAccountActivity extends AppCompatActivity implements Observer, UIErrorHandler {

    private EditText editUserName;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPhoneNumber;

    private String oldUserName;
    private String oldFirstName;
    private String oldLastName;
    private String oldPhoneNumber;

    private Button btnSave;

    private TextWatcher txtWatcher;

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

    public void fillData(){
        //TODO:: make sure session user is correct for drivers.

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

            updateNonCriticalUserFields(newUserName,newFirstName,newLastName,newPhoneNumber);
            this.finish();
        }
        else{
            btnSave.setEnabled(false);
        }
    }
    public void updateNonCriticalUserFields(String newUserName, String newFirstName,
                           String newLastName,String newPhoneNumber){
        User sessionUser = App.getModel().getSessionUser();
        if (sessionUser != null) {
            sessionUser.setUsername(newUserName);
            Account sessionUserAccount = sessionUser.getAccount();
            sessionUserAccount.setFirstName(newFirstName);
            sessionUserAccount.setLastName(newLastName);
            sessionUserAccount.setPhoneNumber(newPhoneNumber);

            App.getController().updateNonCriticalUserFields(sessionUser, this);
        }
    }

    @Override
    public void update(Observable o, Object arg) { }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    public void onError(Error e) {

    }
}
