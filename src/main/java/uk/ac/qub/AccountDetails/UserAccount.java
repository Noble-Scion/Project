 package uk.ac.qub.AccountDetails;

import java.sql.*;


 // UserAccount class representing a user's account with email, password, and user ID.
public class UserAccount {
    private int userId;
    private String username;
    private String password;

    // Constructor
    public UserAccount(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}


