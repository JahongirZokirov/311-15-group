package controllers;

import helpers.OpenWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class Entering {
    @FXML
    void openManagerWindow(ActionEvent event) {

    }

    @FXML
    void openSellerWindow(ActionEvent event) {
        try {
            OpenWindow.openWindow(event, "views/identification.fxml", "Identifikatsiya",
                    400, 500, false, "pictures/Pill_96px.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
