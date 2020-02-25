package com.example.buber.Services;

import com.example.buber.Model.User;

public interface ApplicationServiceHelper {

    User aftersuccessfulLoginofrider(String DOCID);
    User aftersuccessfulCreataAccount(String DOCID);
    User aftersuccessfulLoginofdriver(String DOCID);
}
