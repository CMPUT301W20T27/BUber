package com.example.buber.Views;

/**
 * This interface allows proper error handling for async methods. All activities that
 *  implement this interface can be passed errors bubbled up from the backend.
 */
public interface UIErrorHandler {
    void onError(Error e);
}
