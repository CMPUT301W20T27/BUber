package com.example.buber.Services;
import com.example.buber.DB.AuthDBManager;
import com.example.buber.DB.DBManager;
import com.example.buber.DB.OnUserCreatedListener;
import com.example.buber.Model.Account;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;

public class ApplicationService {
    private static final String TAG = "ApplicationService";

//   -------------------------------EVAN TODO____________________________________________
    // TODO: In the future lets create a seperate AuthService file, for now this is probably ok

    public static void createNewUser(
            String username,
            String password,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            User.TYPE type,
            OnUserCreatedListener onUserCreatedListener
    ) {
        User user = new Rider(username, new Account(firstName, lastName, email));
        AuthDBManager authDBManager = new AuthDBManager();
        authDBManager.createAccount(user, password, onUserCreatedListener);
    } //if it was unsucsessful user = null

}