package uk.ac.qub;
import uk.ac.qub.AccountDetails.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world! (Hello?)");
        UserAccountDAO userAccountDAO = new UserAccountDAO();

        // Replace with the actual user ID you want to retrieve
        int userId = 1;
        UserAccount userAccount = userAccountDAO.getUserAccountById(userId);

        if (userAccount != null) {
            System.out.println("User ID: " + userAccount.getUserId());
            System.out.println("Email: " + userAccount.getUsername());
            System.out.println("Password: " + userAccount.getPassword());
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }

    }
}