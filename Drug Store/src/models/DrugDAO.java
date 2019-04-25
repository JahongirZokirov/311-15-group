package models;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrugDAO {
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

    public static List<Drug> queryAllDrugs() {
        String querySQL = "SELECT * FROM " + TABLE_NAME;
        drugs = new ArrayList<>();

        openConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(querySQL)) {
            while (resultSet.next()) {
                Drug drug = new Drug();
                drug.setDrugID(resultSet.getInt(ID_COl));
                drug.setDrugName(resultSet.getString(NAME_COl));
                drug.setPrice(resultSet.getInt(PRICE_COl));
                drug.setQuantity(resultSet.getInt(QUANTITY_COl));
                drug.setManufactureDate(resultSet.getString(FIRST_DATE_COl));
                drug.setExpireDate(resultSet.getString(SECOND_DATE_COl));
                drug.setCategory(resultSet.getString(CATEGORY_COl));
                drug.setIllness(resultSet.getString(ILLNESS_COl));
                drug.setManufacturer(resultSet.getString(MANUFACTURER_COl));
                // setting image
                InputStream inputStream = resultSet.getBinaryStream(PICTURE_COl);
                Image drugImage = new Image(inputStream);
                drug.setImage(drugImage);
                // add to list
                drugs.add(drug);
            }
            return new ArrayList<>(drugs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConnection();
        }
    }

    public static void updateAmountBoughtParts(ObservableList<Drug> drugs, int totalPrice) {
        int amount, quantity, id;
        openConnection();
        for (Drug buyedDrug : drugs) {
            id = buyedDrug.getDrugID();
            amount = buyedDrug.getAmount();
            quantity = buyedDrug.getQuantity();
            try (Statement statement = connection.createStatement()) {
                statement.execute( "UPDATE " + TABLE_NAME + " SET " + QUANTITY_COl_Str + " = " + (quantity - amount) +
                        " WHERE " + ID_COl_Str + " = " + id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closeConnection();
        SellingDAO.writePurchase(totalPrice);
    }

    public static boolean insertDrug(Drug drug) {
        openConnection();

        // convert image to byte array
        byte[] imageByteArray = toByteArray(drug.getImage());

        String insertSQL = "INSERT INTO " + TABLE_NAME + " VALUES ($next_id, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(2, drug.getDrugName());
            statement.setInt(3, drug.getPrice());
            statement.setInt(4, drug.getQuantity());
            statement.setString(5, drug.getManufactureDate());
            statement.setString(6, drug.getExpireDate());
            statement.setString(7, drug.getCategory());
            statement.setString(8, drug.getIllness());
            statement.setString(9, drug.getManufacturer());
            statement.setBytes(10, imageByteArray);
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
    }

    public static boolean update(int id, Image image, String manufactureDate, String expireDate, int price, int quantity) {
        String updateSQL = "UPDATE "+ TABLE_NAME +" SET "+ PRICE_COl_Str +" = ?,"+ QUANTITY_COl_Str + " = ?,"+
                FIRST_DATE_COl_Str +" = ?,"+ SECOND_DATE_COl_Str +" = ?,"+ PICTURE_COl_Str + " = ?"+
                " WHERE "+ ID_COl_Str +" = ?";
        byte[] bytes = toByteArray(image);
        openConnection();
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setInt(1, price);
            statement.setInt(2, quantity);
            statement.setString(3, manufactureDate);
            statement.setString(4, expireDate);
            statement.setBytes(5, bytes);
            statement.setInt(6, id);
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
    }

    public static boolean updateToZero(int id) {
        String updateZeroSQL = "UPDATE " + TABLE_NAME + " SET " + QUANTITY_COl_Str + " = 0 " + " WHERE " + ID_COl_Str + " = " + id;

        openConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(updateZeroSQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
    }


    public static boolean deleteItem(int id) {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COl_Str + " = " + id;
        openConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(deleteSQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
    }

    private static byte[] toByteArray(Image image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", arrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOutputStream.toByteArray();
    }
}
