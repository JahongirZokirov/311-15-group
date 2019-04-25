package helpers;

import javafx.scene.control.Alert;

public class AlertShow {
    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
