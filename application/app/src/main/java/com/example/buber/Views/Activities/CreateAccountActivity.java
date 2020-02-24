package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buber.R;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{
    // TODO: Implement handler, and call controller
    // TODO: Add in code to correctly interface w/ Model

    private Button btnCreate;

    private EditText editusername;
    private EditText editpassword;
    private EditText editfirstName;
    private EditText editlastName;
    private EditText editemail;
    private EditText editphoneNumber;

    public boolean isValid(){

        if(!(editusername.getText().toString().matches("([A-Za-z0-9]*)"))){
            Toast.makeText(this,"Invalid Username. Username can only contain numbe" +
                    "rs and letters",Toast.LENGTH_LONG).show();
            return false;
        }

        if(!(editemail.getText().toString().matches(".*@.*[.].*"))) {
            Toast.makeText(this,"Invalid email.",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(editphoneNumber.getText().toString().matches("[0-9-]*"))) {
            Toast.makeText(this,"Invalid Phone Number",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_screen);

        btnCreate = findViewById(R.id.createAccountButton);
        btnCreate.setOnClickListener(this);

        editusername = findViewById(R.id.createAccountUsername);
        editpassword = findViewById(R.id.createAccountPassword);
        editfirstName = findViewById(R.id.createAccountFirstName);
        editlastName = findViewById(R.id.createAccountLastName);
        editemail = findViewById(R.id.createAccountEmail);
        editphoneNumber = findViewById(R.id.createAccountPhoneNumber);

    }

    @Override
    public void onClick(View v) {

        if(!isValid()){
            return;
        }

        String username = editusername.getText().toString();
        String password = editpassword.getText().toString();
        String firstName = editfirstName.getText().toString();
        String lastName = editlastName.getText().toString();
        String email = editemail.getText().toString();
        String phoneNumber = editphoneNumber.getText().toString();

        /*

        do stuff with database/model to save data

         */

        Toast.makeText(this,"Account Created Successfully!",Toast.LENGTH_LONG).show();

        this.finish();  //navigate back to log in screen

    }
}
