package uk.ac.qub.AccountDetails;

import java.sql.*;

public class UserAccountDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/UserDB";
    private static final String DB_USER = "csmall11";
    private static final String DB_PASSWORD = "A575586a";

    // Method to retrieve a UserAccount by userId
    public UserAccount getUserAccountById(int userId) {
        UserAccount userAccount = null;
        String query = "SELECT username, password FROM Users WHERE userId = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                userAccount = new UserAccount(userId, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userAccount;
    }
}
