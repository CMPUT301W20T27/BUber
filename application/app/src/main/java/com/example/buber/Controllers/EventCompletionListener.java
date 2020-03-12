package com.example.buber.Controllers;

import java.util.HashMap;

/**
 * Interface used to properly deal with an asynchronous MVC architecture. Provides the ability
 * for asynchronous code to call elsewhere in the codebase passing result data via the resultData
 * hashmap, and supports error handling via the 'err' parameter.
 */
public interface EventCompletionListener {
    void onCompletion(HashMap<String, ?> resultData, Error err);
}
