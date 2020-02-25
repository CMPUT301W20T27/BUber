package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.R;
import com.example.buber.Services.ApplicationService;

import java.util.Observable;
import java.util.Observer;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener, Observer {

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
        App.getModel().addObserver(this);

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

        DBManager DB = new DBManager();
        DB.createAccount();
        Toast.makeText(this,"Madeeha was here",Toast.LENGTH_LONG).show();
//        if(isValid()){
//            String username = editusername.getText().toString();
//            String password = editpassword.getText().toString();
//            String firstName = editfirstName.getText().toString();
//            String lastName = editlastName.getText().toString();
//            String email = editemail.getText().toString();
//            String phoneNumber = editphoneNumber.getText().toString();
//
//            App.getController().createNewUser(username, password, firstName, lastName, email, phoneNumber);
//            Toast.makeText(this,"Account Created Successfully!",Toast.LENGTH_LONG).show();
////            this.finish();  //navigate back to log in screen
//
//
//



//        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        ApplicationModel m = (ApplicationModel) o;
        if (m.getSessionUser() != null) {
            // TODO: Start MapActivity, Remove loginActivity from view stack
        }
    }
}
