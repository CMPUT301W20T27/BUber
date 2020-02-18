package com.example.buber.Controllers;

import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.Services.*;

public class ApplicationController {
    ApplicationModel model;


    public ApplicationController(ApplicationModel model) {
        this.model = model;
    }


    public void initModel() {
        // TODO: This method @ application start, should populate our model with
        // any application state that the controller needs to get from the DB
        // the model should then notifyallviews
    }
}
