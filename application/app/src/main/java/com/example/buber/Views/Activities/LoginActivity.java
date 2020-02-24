package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.buber.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    // TODO: Implement handler, perform validation and call controller
    // TODO: Add in code to correctly interface w/ Model
    private Button createAccountBtn;
    private Button loginAsDriverBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        createAccountBtn = findViewById(R.id.loginCreateAccountButton);
        loginAsDriverBtn = findViewById(R.id.loginLoginButton);
        createAccountBtn.setOnClickListener(this);
        loginAsDriverBtn.setOnClickListener(this);
    }

    public boolean isValidAccount(){

        /*

        Do stuff to verify account.

         */

        return false;
    }
    @Override
    public void onClick(View view){

        Log.d("CLICK","clicked a button");

        if(view.getId()==R.id.loginCreateAccountButton) {
            Log.d("CREATE","Starting create");
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        }

        else if(view.getId()==R.id.loginLoginButton) {

            if(isValidAccount()) {
                startActivity(new Intent(LoginActivity.this, MapActivity.class));
            }
            else{
                Toast.makeText(this,"Invalid Account/Credentials",Toast.LENGTH_SHORT).show();
                return;
            }

        }

    }
}
