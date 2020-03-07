package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.buber.App;
import com.example.buber.Model.Account;
import com.example.buber.Model.User;
import com.example.buber.R;

import java.util.Observable;
import java.util.Observer;

public class EditAccountActivity extends AppCompatActivity implements Observer {

    private EditText editUserName;
    private EditText editpassword;
    private EditText editfirstName;
    private EditText editlastName;
    private EditText editEmail;
    private EditText editphoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        editUserName = findViewById(R.id.createAccountUsername);
        editpassword = findViewById(R.id.editAccountPassword);
        editfirstName = findViewById(R.id.createAccountFirstName);
        editlastName = findViewById(R.id.createAccountLastName);
        editEmail = findViewById(R.id.createAccountEmail);
        editphoneNumber = findViewById(R.id.createAccountPhoneNumber);

        fillData();
    }

    public void fillData(){
        User curUser = App.getModel().getSessionUser();
        String userName = curUser.getUsername();
        String firstName = curUser.getAccount().getFirstName();
        String lastName = curUser.getAccount().getLastName();
        String email = curUser.getAccount().getEmail();
        String phoneNumber = curUser.getAccount().getPhoneNumber();

        editUserName.setText(userName);
        editEmail.setText(email);
        editfirstName.setText(firstName);
        editlastName.setText(lastName);
        editphoneNumber.setText(phoneNumber);
    }

    public void handleEditAccountSaveClick(View v){

    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO:: fill this in
    }
}
