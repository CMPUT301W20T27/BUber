package com.example.buber.Model;

/**
 * Contains user account information / non-application user metadata.
 */
public class Account {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    /**
     * Empty constructor used for Firebase
     */
    public Account() {}

    /**
     *Account constructor
     * @param firstName,lastName,email,phoneNumber the users Account information
     */
    public Account(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
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

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber(){return phoneNumber;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
