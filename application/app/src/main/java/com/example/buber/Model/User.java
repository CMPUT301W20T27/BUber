package com.example.buber.Model;

public abstract class User {
//    private Contact contact;        //not sure about this so commented for now
    private Location currentLocation;
    private Account account;
    private String username;

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

//    public Contact getContact() {
//        return contact;
//    }
//
//    public void setContact(Contact contact) {
//        this.contact = contact;
//    }

}
