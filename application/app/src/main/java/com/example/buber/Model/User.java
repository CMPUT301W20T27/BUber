package com.example.buber.Model;

/**
 * Represents a User. Stores account information, type (Rider or Driver), username and other
 * information.
 */
public abstract class User {
    public enum TYPE {
        DRIVER,
        RIDER,
    }

    public User(String username, Account account) {
        this.username = username;
        this.account = account;
    }

    public User() { }

    private UserLocation currentUserLocation;
    private Account account;
    private String username;
    private TYPE type;

    public UserLocation getCurrentUserLocation() {
        return currentUserLocation;
    }

    public void setCurrentUserLocation(UserLocation currentUserLocation) {
        this.currentUserLocation = currentUserLocation;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

}
