package com.example.buber.Views.Activities.Models;
import androidx.annotation.NonNull;
/**
 * A simple Driver class that holds the Driver properties and is the link to Fb "Diver" collection
 */
public class Driver {

    private  String firstName;
    private  String lastName;
    private  String userID;

    public Driver(){}       // VERY IMPORTANT TO HAVE


    public Driver(String firstName, String lastName, String userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @NonNull
    @Override
    public String toString() {
        return "First Name: " + firstName + " Last Name: " + lastName + " User ID: " +  userID;
    }
}
