package com.example.buber;

import android.app.Application;

import com.example.buber.Controllers.ApplicationController;
import com.example.buber.DB.AuthDBManager;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.ApplicationModel;

public class App extends Application {

    // Only should contain the pieces of the MVC. App class should be responsbile
    // for pullup/teardown of the application
    private static final String DRIVERS_COLLECTION_NAME = "Drivers";
    private static final String RIDERS_COLLECTION_NAME = "Riders";
    private static final String TRIPS_COLLECTION_NAME = "Trips";
    transient private static ApplicationModel model;
    transient private static ApplicationController controller;
    transient private static AuthDBManager authDBManager;
    transient private static DBManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        getModel();
        getController();
        getAuthDBManager();
        getDbManager();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        model = null;
        controller = null;
        dbManager = null;
        authDBManager = null;
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

    public static AuthDBManager getAuthDBManager() {
        if (authDBManager == null) {
            authDBManager = new AuthDBManager();
        }

        return authDBManager;
    }

    public static DBManager getDbManager() {
        if (dbManager== null) {
            dbManager = new DBManager(DRIVERS_COLLECTION_NAME, RIDERS_COLLECTION_NAME, TRIPS_COLLECTION_NAME);
        }

        return dbManager;
    }

}
