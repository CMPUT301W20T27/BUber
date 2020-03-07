package com.example.buber.Services;
import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.Account;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;

import java.util.HashMap;

public class ApplicationService {
    private static final String TAG = "ApplicationService";

    public static void createNewUser(
            String username,
            String password,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            User.TYPE type,
            EventCompletionListener controllerListener
    ) {

        Account newUserAccount = new Account(firstName, lastName, email, phoneNumber);
        App.getAuthDBManager().createFirebaseUser(email, password, (resultData, err) -> {
            if (err != null) {
                controllerListener.onCompletion(null, new Error(err.getMessage()));
                return;
            }
            String docID = (String) resultData.get("doc-id");
            // Right now, we just return a rider object, this should change if we provide the
            // option to login as both
            App.getDbManager().createRider(docID, new Rider(username, newUserAccount), controllerListener);
            App.getDbManager().createDriver(docID, new Driver(username, newUserAccount), ((resultData1, err1) -> {}));

        });

    }

    public static void loginUser(String email,
                                 String password,
                                 User.TYPE type,
                                 EventCompletionListener controllerListener) {
       App.getAuthDBManager().signIn(email, password, (resultData, err) -> {
           if (err != null) {
               controllerListener.onCompletion(null, new Error(err.getMessage()));
               return;
           }
           String docID = (String) resultData.get("doc-id");
           if (type == User.TYPE.DRIVER) {
               App.getDbManager().getDriver(docID, controllerListener);
           } else {
               App.getDbManager().getRider(docID, controllerListener);
           }
       });
    }

    public static void logoutUser() {
        App.getAuthDBManager().signOut();
    }

    public static void updateUser(User updateSessionUser, EventCompletionListener listener) {
        // TODO: Get the Uid of the user using lukes function he wrote in AuthDBManger and complete
        //  These functions
        App.getDbManager().updateRider(updateSessionUser, new EventCompletionListener() {
            @Override
            public void onCompletion(HashMap<String, ?> resultData, Error err) {
                if (err == null) {
                    App.getDbManager().updateDriver(updateSessionUser, new EventCompletionListener() {
                        @Override
                        public void onCompletion(HashMap<String, ?> resultData, Error err) {
                            if (err == null) {
                                listener.onCompletion(null, null);
                            }
                        }
                    });
                }
            }
        });
    }

}