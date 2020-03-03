package com.example.buber.Services;
import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Account;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;

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
            String docID = (String) resultData.get("doc-id");
            if (type == User.TYPE.DRIVER) {
                App.getDbManager().createDriver(docID, new Driver(username, newUserAccount), controllerListener);
            } else {
                App.getDbManager().createRider(docID, new Rider(username, newUserAccount), controllerListener);
            }
        });

    }

    public static void loginUser(String email,
                                 String password,
                                 User.TYPE type,
                                 EventCompletionListener controllerListener) {
       App.getAuthDBManager().signIn(email, password, (resultData, err) -> {
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

}