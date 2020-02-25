package com.example.buber.Services;
import com.example.buber.DB.AuthDBManager;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;

public class ApplicationService {
//  // TODO: Lukes code go here
//   -------------------------------EVAN TODO____________________________________________
    // TODO: In the future lets create a seperate AuthService file, for now this is probably ok

    // TODO: Implement
    public static User signIn(String username, String password, User.TYPE type) {
        if (type == User.TYPE.RIDER) {
            return new Rider();
        }
        return new Driver();
    }

    // TODO: Implement
    public static void signOut(User user) {
    }

    // TODO: Implement, make sure you have all information you need
    public static User createNewUser(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            String phoneNumber


            // they want me to create a new user aka an account
    ) {
        return null;
    } //if it was unsucsessful

//    private DBManager DB = new DBManager();
//
//    public void createAccountService() {
//
//        /*TODO: TODO: Get username and password from UI when user creates an account*/
//        DB.login.createAccount("madeehanumber2@BUber.com", "123456", this);    // If account is created aftersuccessfulCreataAccount() is called
//    }


//    public User aftersuccessfulCreataAccount(String DocID){
//        //- is it a Rider.class or driver.class? or both?
//        Rider riderObj = new Rider();
//        Driver driverObj = new Driver();
//
//        DB.createDriver(DocID, driverObj); //here we created an empty document in the rider or driver collection
//        DB.createRider(DocID, riderObj);
////        or we can pass data into the document
////        updateDriver(Document ID, Location ect...)
//
//        return (User) riderObj; //or (User) driverObj;
//
//    }
//
//}


//
//    public void login(){
//
//        login.signIn("madeeha@uber.com", "123456", this);
//    }
//
//
//    public User aftersuccessfulLoginofrider(String DOCID){
//        login.getcurrentUser();
//        //get rider using doc id from DBManager
//        Rider riderObj = new Rider();
//
//        return (User) riderObj;
//
//    }
//
//    public User aftersuccessfulLoginofdriver(String DOCID){
//
//        Driver driverObj = new Driver();
//
//        return (User) driverObj ;
//    }
}
