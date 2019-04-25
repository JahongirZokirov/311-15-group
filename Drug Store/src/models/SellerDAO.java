package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellerDAO {
    private static List<Seller> sellers;
    private static final String DBNAME = "drugstore.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DBNAME;
    private static Connection connection;
    private static String TABLE_NAME = "Stuff";
    private static final int ID_COL = 1;
    private static final int NAME_COL = 2;
    private static final int PASSWORD_COL = 3;
    private static final int EMAIL_COL = 4;
    private static final int RANK_COL = 5;
    private static final int SALARY_COL = 6;

    public static List<Seller> queryAllSellers() {
        sellers = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME;
        Seller seller;

        openConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {
            while (resultSet.next()) {
                seller = new Seller();
                seller.setId(resultSet.getInt(ID_COL));
                seller.setFullName(resultSet.getString(NAME_COL));
                seller.setPassword(resultSet.getString(PASSWORD_COL));
                seller.setEmail(resultSet.getString(EMAIL_COL));
                seller.setRank(resultSet.getInt(RANK_COL));
                seller.setSalary(resultSet.getDouble(SALARY_COL));
                sellers.add(seller);
            }

            return new ArrayList<>(sellers);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
