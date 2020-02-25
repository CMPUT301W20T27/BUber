package com.example.buber.Services;

import com.example.buber.Model.User;
import com.google.firebase.auth.FirebaseUser;

public interface ApplicationServiceHelper {

    User aftersuccessfulLoginofrider(FirebaseUser user);
    User aftersuccessfulCreataAccount(FirebaseUser user);
    User aftersuccessfulLoginofdriver(FirebaseUser user);
}
