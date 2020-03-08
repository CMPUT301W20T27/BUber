package com.example.buber.Views.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer, UIErrorHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO:database connections, model, etc..
        super.onCreate(savedInstanceState);
        ApplicationModel m = App.getModel();
        m.addObserver(this);
        handleLoginStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        ApplicationModel m = (ApplicationModel) o;
    }

    private void handleLoginStatus() {
        if(App.getAuthDBManager().isLoggedIn()) {
            Log.d("LOGIN","Already logged in");
            App.getAuthDBManager().getCurrentSessionUser((resultData, err) -> {
                if (resultData != null) {
                    App.getModel().setSessionUser((User) resultData.get("user"));
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                } else {
                    Toast.makeText(this, err.getMessage(), Toast.LENGTH_LONG);
                }
            });

        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        this.finish();
    }

    @Override
    public void onError(Error e) {

    }
}
