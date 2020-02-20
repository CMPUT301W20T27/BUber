package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.buber.R;

public class Login extends AppCompatActivity implements View.OnClickListener{

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
        startActivity(new Intent(Login.this,CreateAccountActivity.class));
    }
}
