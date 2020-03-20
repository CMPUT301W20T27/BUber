package com.example.buber;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

import com.example.buber.Controllers.ApplicationController;
import com.example.buber.DB.AuthDBManager;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.ApplicationModel;

/**
 * Root class of our application. Extends the base Android.app.Application class.
 * Each application layer is housed as a singleton.
 */
public class App extends Application {

    // Only should contain the pieces of the MVC. App class should be responsible
    // for pull up/teardown of the application
    private static final String DRIVERS_COLLECTION_NAME = "Drivers";
    private static final String RIDERS_COLLECTION_NAME = "Riders";
    private static final String TRIPS_COLLECTION_NAME = "Trips";
    transient private static ApplicationModel model;
    transient private static ApplicationController controller;
    transient private static AuthDBManager authDBManager;
    transient private static DBManager dbManager;

    // Notification Channels
    public static final String DRIVER_CHANNEL_ID = "DRIVER_CHANNEL";
    public static final String RIDER_CHANNEL_ID = "RIDER_CHANNEL";


    @Override
    public void onCreate() {
        super.onCreate();
        getModel();
        getController();
        getAuthDBManager();
        getDbManager();
        createNotificationChannels();
    }

    /**
     * When the app closes remove trip listeners, and terminate classes
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        model = null;
        controller = null;
        dbManager = null;
        authDBManager = null;

        // Remove any triplisteners the app may have
        if (model.getTripListener() != null) {
            model.getTripListener().remove();
        }
    }
    /**/

    /**
     * Create a single Model
     */
    public static ApplicationModel getModel() {
        if (model == null) {
            model = new ApplicationModel();
        }

        return model;
    }

    /**
     * Create a single Controller
     */
    public static ApplicationController getController() {
        if (controller == null) {
            controller = new ApplicationController(getModel());
        }

        return controller;
    }

    /**
     * Create a single Firebase authorization class
     */

    public static AuthDBManager getAuthDBManager() {
        if (authDBManager == null) {
            authDBManager = new AuthDBManager();
        }

        return authDBManager;
    }

    /**
     * Create a single Firebase database manager class
     */
    public static DBManager getDbManager() {
        if (dbManager == null) {
            dbManager = new DBManager(DRIVERS_COLLECTION_NAME, RIDERS_COLLECTION_NAME, TRIPS_COLLECTION_NAME);
        }

        return dbManager;
    }


    /**
     * Creates our notification channels
     * Only supported from Android Oreo and greater
     */
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel driverChannel = new NotificationChannel(
                    DRIVER_CHANNEL_ID,
                    "Driver Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            driverChannel.setDescription("This is the driver channel.");

            NotificationChannel riderChannel = new NotificationChannel(
                    RIDER_CHANNEL_ID,
                    "Rider Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            riderChannel.setDescription("This is the rider channel.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(driverChannel);
            manager.createNotificationChannel(riderChannel);
        }
    }
}
