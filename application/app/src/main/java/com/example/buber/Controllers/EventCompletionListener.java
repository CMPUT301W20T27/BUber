package com.example.buber.Controllers;

import com.example.buber.Model.Driver;
import com.example.buber.Model.User;

import java.util.HashMap;

public interface EventCompletionListener {
    void onCompletion(HashMap<String, ?> resultData, Error err);
}
