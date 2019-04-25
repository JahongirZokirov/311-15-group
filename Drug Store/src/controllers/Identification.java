package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import helpers.AlertShow;
import helpers.HashPassword;
import helpers.OpenWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import models.Seller;
import models.SellerDAO;

import java.io.IOException;
import java.util.List;

public class Identification {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    private int counter;
    private boolean access;

    @FXML
    void initialize() {
        counter = 0;
        access = false;
    }

    @FXML
    void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        List<Seller> sellers = SellerDAO.queryAllSellers();

        if (counter >= 5) {
            AlertShow.showAlert(Alert.AlertType.ERROR, "Xatolik", "5 ta uvaqqiyatsiz urinish", "");
            forgetPassword(event);
        }

        if (sellers != null) {
            for (Seller seller : sellers) {
                if ((username.equalsIgnoreCase(seller.getFullName()) || username.equalsIgnoreCase(seller.getEmail())) &&
                        seller.getPassword().equals(HashPassword.hashCode(password, HashPassword.SHA1))) {
                    access = true;
                    try {
                        OpenWindow.openWindow(event, "views/shop.fxml", "Main",
                                1300, 680, true, "pictures/Clinic_48px.png");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        counter++;

        if (!access && counter < 5) {
            AlertShow.showAlert(Alert.AlertType.WARNING, "Warning", "Xatolik",
                    "Login yoki paro xato");
        }
    }

    @FXML
    void forgetPassword(ActionEvent event) {
        try {
            OpenWindow.openWindow(event, "views/forgetPassoword.fxml", "Parolni tiklash", 400, 300, false,
                    "pictures/Key 2_48px.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
