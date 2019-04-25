package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SellingDAO {
    private static List<Seller> sellings;
    private static final String DBNAME = "drugstore.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DBNAME;
    private static Connection connection;
    private static final String TABLE_NAME = "Sellings";
    private static final String AMOUNT_COL = "Amount";
    private static final String DATE_COL = "Date";

    public static void writePurchase(int totalPrice) {
        openConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO "+ TABLE_NAME +" ( "+ AMOUNT_COL +", "+ DATE_COL +") VALUES ("+ totalPrice +", datetime('now', 'localtime'))");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
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
