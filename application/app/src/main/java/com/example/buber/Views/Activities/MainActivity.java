package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.R;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // HOW TO GET THE MODEL IN ANY VIEW, DO NOT DIRECTLY UPDATE
        ApplicationModel m = App.getModel();
        // HOW TO GET THE CONTROLLER IN ANY VIEW
        ApplicationController c = App.getController();
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
        // USE THIS TO UPDATE THE VIEW
        ApplicationModel m = (ApplicationModel) o;
    }


}
