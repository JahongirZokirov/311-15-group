package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PasswordGenerator {
    public static final String DBNAME = "drugstore.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DBNAME;
    private static Connection connection;

    public static void main(String[] args) {
        String[] passwords = {"pass", "password", "12345"};
        String hashPasword;
        public static void main(String[] args) {
            String[] passwords = {"pass", "password", "12345"};
            String hashPasword; public static void main(String[] args) {
                String[] passwords = {"pass", "password", "12345"};
                String hashPasword;
        openConnection();
        for (int i = 0; i < passwords.length; i++) {
            hashPasword = HashPassword.hashCode(passwords[i], HashPassword.SHA1);
            try (Statement statement = connection.createStatement();) {
                statement.executeUpdate("UPDATE Stuff SET Password = '" + hashPasword + "' WHERE EmployeeID = " + (i+1));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void openConnection() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
