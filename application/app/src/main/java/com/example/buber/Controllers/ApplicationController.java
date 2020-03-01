package com.example.buber.Controllers;
import com.example.buber.DB.OnUserCreatedListener;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.Services.*;

public class ApplicationController {
    private ApplicationModel model;
    // TODO: In the future, whenever we interface with the DB, we need to do so in a new
    // thread to avoid performance bottlenecks

    public ApplicationController(ApplicationModel model) {
        this.model = model;
    }

    public void initModel() {
        // TODO: This method @ application start, should populate our model with
        // any application state that the controller needs to get from the DB
        // the model should then notifyallviews
    }

    public void createNewUser(String username, String password, String firstName, String lastName, String email, String phoneNumber, User.TYPE type) {

        // Cannot return a user cuz asynchronous reasons
        // User sessionUser = ApplicationService.createNewUser(username, password, email, firstName, lastName, phoneNumber);
        ApplicationService.createNewUser(
                username,
                password,
                firstName,
                lastName,
                email,
                phoneNumber,
                type,
                new OnUserCreatedListener() {
                    @Override
                    public void onUserCreated(User sessionUser) {
                        model.setSessionUser(sessionUser);
                    }
                }
        );
    }
}
