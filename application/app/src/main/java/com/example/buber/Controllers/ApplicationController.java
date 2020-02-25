package com.example.buber.Controllers;

import com.example.buber.App;
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

    public void login(String username, String password, User.TYPE type) {
        User sessionUser = ApplicationService.signIn(username, password, type);
        model.setSessionUser(sessionUser);
    }

    public void logout() {
        User sessionUser = model.getSessionUser();
        ApplicationService.signOut(sessionUser);
        model.setSessionUser(null);
    }

    public void createNewUser(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            String phoneNumber
    ) {
        User sessionUser = ApplicationService.createNewUser(username, password, email, firstName, lastName, phoneNumber);
        model.setSessionUser(sessionUser);
    }
}
