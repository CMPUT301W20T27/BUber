package com.example.buber.Services;

import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;

public class ApplicationService {
    // TODO: In the future lets create a seperate AuthService file, for now this is probably ok

    // TODO: Implement
    public static User signIn(String username, String password, User.TYPE type) {
        if (type == User.TYPE.RIDER) {
            return new Rider();
        }
        return new Driver();
    }

    // TODO: Implement
    public static void signOut(User user) {}

    // TODO: Implement, make sure you have all information you need
    public static User createNewUser(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            String phoneNumber
    ) { return null; }

}
