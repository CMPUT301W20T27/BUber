package com.example.buber.Controllers;

import java.util.HashMap;

/**
 * Interface used to properly deal with an asynchronous MVC architecture. Provides the ability
 * for asynchronous code to call elsewhere in the codebase passing result data via the resultData
 * hashmap, and supports error handling via the 'err' parameter.
 */
public interface EventCompletionListener {
    /**
     * When the event is complete overrider this method
     * @param resultData is a hashmap entry with any type associated with it
     * @param err can take in an error
     * */
    void onCompletion(HashMap<String, ?> resultData, Error err);
}
