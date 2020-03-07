package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.Account;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.Activities.FormUtilities.CreateAccountFormUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;
//import com.gc.materialdesign.views.Button;
//import com.google.api.core;

import java.util.Observable;
import java.util.Observer;

public class EditAccountActivity extends AppCompatActivity implements Observer {

    private EditText editUserName;
    private EditText editpassword;
    private EditText editfirstName;
    private EditText editlastName;
    private EditText editEmail;
    private EditText editphoneNumber;

    private String olduserName;
    private String oldfirstName;
    private String oldlastName;
    private String oldemail;
    private String oldphoneNumber;

    private Button btnSave;

    private TextWatcher txtWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        App.getModel().addObserver(this);
        editUserName = findViewById(R.id.createAccountUsername);
        editpassword = findViewById(R.id.editAccountPassword);
        editfirstName = findViewById(R.id.createAccountFirstName);
        editlastName = findViewById(R.id.createAccountLastName);
        editEmail = findViewById(R.id.createAccountEmail);
        editphoneNumber = findViewById(R.id.createAccountPhoneNumber);
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

        olduserName = curUser.getUsername();
        oldfirstName = curUser.getAccount().getFirstName();
        oldlastName = curUser.getAccount().getLastName();
        oldemail = curUser.getAccount().getEmail();
        oldphoneNumber = curUser.getAccount().getPhoneNumber();

        editUserName.setText(olduserName);
        editEmail.setText(oldemail);
        editfirstName.setText(oldfirstName);
        editlastName.setText(oldlastName);
        editphoneNumber.setText(oldphoneNumber);

        editUserName.addTextChangedListener(txtWatcher);
        editphoneNumber.addTextChangedListener(txtWatcher);
        editlastName.addTextChangedListener(txtWatcher);
        editfirstName.addTextChangedListener(txtWatcher);
        editEmail.addTextChangedListener(txtWatcher);
        editpassword.addTextChangedListener(txtWatcher);

    }

    public void handleEditAccountSaveClick(View v) {
        if (CreateAccountFormUtils.validateForm(
                editUserName,
                editpassword,
                editfirstName,
                editlastName,
                editEmail,
                editphoneNumber)) {

            String newUserName = editUserName.getText().toString().trim();
            String newPassword = editpassword.getText().toString().trim();
            String newFirstName = editfirstName.getText().toString().trim();
            String newLastName = editlastName.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();
            String newPhoneNumber = editphoneNumber.getText().toString().trim();

            //TODO: update user in the DB
            updateUser(newUserName,newPassword,newFirstName,newLastName,newEmail,newPhoneNumber);
            this.finish();


        }
        else{
            btnSave.setEnabled(false);
        }
    }
    public void updateUser(String newUserName, String newPassword,String newFirstName,
                           String newLastName,String newEmail,String newPhoneNumber){

        DBManager dbManager = App.getDbManager();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        User curUser =App.getModel().getSessionUser();
        Account account = curUser.getAccount();
        String docID = auth.getUid();
        Log.d("DATATEST",docID);
        account.setFirstName(newFirstName);
        account.setLastName(newLastName);
        account.setPhoneNumber(newPhoneNumber);
        curUser.setUsername(newUserName);
        //TODO: maybe its not a good idea to allow changing email
        Rider newRider = new Rider(newUserName,account);

        //dbManager.updateRider(docID,newRider,listener);



    }
    @Override
    public void update(Observable o, Object arg) {
        //TODO:: fill this in
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }
}
