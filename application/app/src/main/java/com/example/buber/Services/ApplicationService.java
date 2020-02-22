package com.example.buber.Services;
import com.example.buber.DB.AuthDBManager;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;

public class ApplicationService implements  ApplicationServiceHelper{
//  // TODO: Lukes code go here
//   -------------------------------EVAN TODO____________________________________________
    // TODO: In the future lets create a seperate AuthService file, for now this is probably ok

    // TODO: Implement
    public static User signIn(String username, String password) { return null; }

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


//    --------------------MADEEHA---------------------------------------
    /*TODO:
       //get username and password from UI when user creates an account
       //append @Uber.com to the username
       //call my create account function in LoginAuth:              LoginAuth.createAccount(username@uber.com, password, (method called on sucess))
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
            }

     */
    //--------------------------------------------Testing------------------------------------------
    private AuthDBManager login = new AuthDBManager();
    private DBManager DB = new DBManager();

    public void createAccountService(){
        /*TODO: TODO: Get username and password from UI when user creates an account*/
        login.createAccount("madeeha@BUber.com", "123456" , this);    // If account is created aftersuccessfulCreataAccount() is called
    }


    public User aftersuccessfulCreataAccount(String DocID){
        //- is it a Rider.class or driver.class? or both?
        Rider riderObj = new Rider();
        Driver driverObj = new Driver();

        DB.createDriver(DocID, driverObj); //here we created an empty document in the rider or driver collection
        DB.createRider(DocID, riderObj);
//        or we can pass data into the document
//        updateDriver(Document ID, Location ect...)

        return (User) riderObj; //or (User) driverObj;

    }




}
