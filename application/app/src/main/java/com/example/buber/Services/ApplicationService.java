package com.example.buber.Services;

public class ApplicationService {
    // TODO: Lukes code go here




    /*TODO:
       //get username and password from UI when user creates an account
       //append @Uber.com to the username
       //call my create account function in LoginAuth:              LoginAuth.createAccount(username@uber.com, password)
            if this returns boolean true(the username is unique and not already in use){
                    To finish creating the account we need from UI:
                    - is it a Rider.class or driver.class? or both?
                       Get the document ID:  LoginAuth.getcurrentUserDocID()
                      and Create : DatabaseManager.createRider( document ID) or   DatabaseManager.createDriver( document ID)                //here we created an empty document in the rider or driver collection
                   Next populate/Create a rider or Driver class object (extend user):
                    User class has:
                    -Contact                                           //I commented out cause im not sure about it
                    -Location.class of the rider or driver:             Location(private float latitude, private float longitude);
                    -Account.class                                      Account (firstName, lastName, email(getcurrentUser().getEmail());
                    -String username;                                   String getcurrentUser().getEmail()- minus @uber.com(Email is the username with @uber.com appended)
                  Rider has no other attributes
                  Driver has ratting?
            Once the rider or driver class has been created update the driver or rider with data                                                  //here we populate the empty document in the rider or driver collection
                updateDriver(Document ID)
                ***cast the Driver as a user object;
                now we have a current user
            else error
            }



     */


}
