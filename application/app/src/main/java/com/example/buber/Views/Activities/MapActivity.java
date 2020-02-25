package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.R;

import java.util.Observable;
import java.util.Observer;

public class MapActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        App.getModel().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO: Update Map View
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }
}
