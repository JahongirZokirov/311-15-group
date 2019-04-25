package helpers;

import models.Drug;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.time.temporal.ChronoUnit.DAYS;


public class DataGenerator {
    private static List<Drug> drugs;
    private static final String DBNAME = "drugstore.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DBNAME;
    private static Connection connection;
    private static final String TABLE_NAME = "Medicine";
    private static final int ID_COl = 1;
    private static final String ID_COl_Str = "DrugID";
    private static final int NAME_COl = 2;
    private static final int PRICE_COl = 3;
    private static final String PRICE_COl_Str = "Price";
    private static final int QUANTITY_COl = 4;
    private static final String QUANTITY_COl_Str = "Quantity";
    private static final int FIRST_DATE_COl = 5;
    private static final String FIRST_DATE_COl_Str = "ManufactureDate";
    private static final int SECOND_DATE_COl = 6;
    private static final String SECOND_DATE_COl_Str = "ExpireDate";
    private static final int CATEGORY_COl = 7;
    private static final int ILLNESS_COl = 8;
    private static final int MANUFACTURER_COl = 9;
    private static final int PICTURE_COl = 10;
    private static final String PICTURE_COl_Str = "Picture";

    public static void main(String[] args) {
        int quantity, price;
        LocalDate beginDate = LocalDate.parse("2016-01-01");
        LocalDate now = LocalDate.now();
        LocalDate endDate = LocalDate.parse("2022-01-01");
        long forManufacture = DAYS.between(beginDate, now);
        long forExpire = DAYS.between(beginDate, endDate);
        String updateSQL = "UPDATE "+ TABLE_NAME +" SET "+ PRICE_COl_Str +" = ?,"+ QUANTITY_COl_Str + " = ?,"+
                FIRST_DATE_COl_Str +" = ?,"+ SECOND_DATE_COl_Str +" = ?" +
                " WHERE "+ ID_COl_Str +" = ?";

        openConnection();
        for (int i = 1; i <= 20; i++) {
            quantity = ThreadLocalRandom.current().nextInt(0, 10001);
            price = ThreadLocalRandom.current().nextInt(0, 150001);
            LocalDate manufactureDate = beginDate.plusDays(ThreadLocalRandom.current().nextLong(forManufacture + 1));
            LocalDate expireDate = beginDate.plusDays(ThreadLocalRandom.current().nextLong(forExpire + 1));
            try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
                statement.setInt(1, price);
                statement.setInt(2, quantity);
                statement.setString(3, manufactureDate.toString());
                statement.setString(4, expireDate.toString());
                statement.setInt(5, i);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closeConnection();
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
