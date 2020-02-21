package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.buber.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    // TODO: Implement handler, perform validation and call controller
    // TODO: Add in code to correctly interface w/ Model

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button createAccountBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        createAccountBtn = findViewById(R.id.loginCreateAccountButton);
        createAccountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
    }
}
