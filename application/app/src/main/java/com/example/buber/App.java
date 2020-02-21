package com.example.buber;

import android.app.Application;

import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;

import java.util.ArrayList;
import java.util.Observer;

public class App extends Application {

    // Only should contain the pieces of the MVC. App class should be responsbile
    // for pullup/teardown of the application
    transient private static ApplicationModel model;
    transient private static ApplicationController controller;
    transient private ArrayList<Observer> views;

    @Override
    public void onCreate() {
        super.onCreate();
        getModel();
        getController();
        // init
        init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // TODO: Remove anything that should be removed
        model = null;
        controller = null;
    }

    public static ApplicationModel getModel() {
        if (model == null) {
            model = new ApplicationModel();
        }

        return model;
    }

    public static ApplicationController getController() {
        if (controller == null) {
            controller = new ApplicationController(getModel());
        }

        return controller;
    }

    private void init() {
        // Initialize the model
        controller.initModel();

        // Hook up views to model
        for (Observer o : views) {
            model.addObserver(o);
        }
    }

}
