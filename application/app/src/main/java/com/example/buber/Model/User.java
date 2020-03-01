package com.example.buber.Model;


public abstract class User {
    public enum TYPE {
        //Firebase clollection names
        Drivers,
        Riders,
    }

    public User(String username, Account account) {
        this.username = username;
        this.account = account;
    }

    public User() {
    }

    //    private Contact contact;        //Future implimentation
    private Location currentLocation;
    private Account account;
    private String username;
    private TYPE type;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
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
