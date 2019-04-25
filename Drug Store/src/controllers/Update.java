package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import helpers.AlertShow;
import helpers.OpenWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Drug;
import models.DrugDAO;

import java.io.*;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

public class Update {

    @FXML
    private ImageView productImage;

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField priceField;

    @FXML
    private JFXTextField illnessField;

    @FXML
    private JFXTextField manufacturerField;

    @FXML
    private JFXTextField categoryField;

    @FXML
    private JFXTextField quantityField;

    @FXML
    private JFXDatePicker manufactureDate;

    @FXML
    private JFXDatePicker expireDate;

    @FXML
    private StackPane stackPane;

    private static Drug selectedDrug;

    @FXML
    void initialize() {
        NumberValidator validator = new NumberValidator();
        validator.setMessage("Faqat butun son!");
        quantityField.getValidators().add(validator);
        quantityField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                quantityField.validate();
            }
        });

        NumberValidator validator1 = new NumberValidator();
        validator1.setMessage("Faqat butun son!");
        priceField.getValidators().add(validator1);
        priceField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                priceField.validate();
            }
        }));

        selectedDrug = Shop.getSelectedDrug();
        // setting data
        productImage.setImage(selectedDrug.getImage());
        manufactureDate.setValue(LocalDate.parse(selectedDrug.getManufactureDate()));
        expireDate.setValue(LocalDate.parse(selectedDrug.getExpireDate()));
        nameField.setText(selectedDrug.getDrugName());
        priceField.setText(selectedDrug.getPrice() + "");
        illnessField.setText(selectedDrug.getIllness());
        manufacturerField.setText(selectedDrug.getManufacturer());
        categoryField.setText(selectedDrug.getCategory());
        quantityField.setText(String.valueOf(selectedDrug.getQuantity()));
    }

    @FXML
    void backToMain(ActionEvent event) {
        try {
            OpenWindow.openWindow(event, "views/shop.fxml", "Main",
                    1300, 680, true, "pictures/Clinic_48px.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void browsePicture() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG", "*.JPEG");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(extensionFilterJPG, extensionFilterPNG);
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            String path = file.toURI().toURL().toString();
            Image image = new Image(Objects.requireNonNull(path));
            productImage.fitHeightProperty();
            productImage.fitWidthProperty();
            productImage.setImage(image);
        }
    }

    @FXML
    void checkDate() {
        LocalDate firstDate = manufactureDate.getValue();
        LocalDate secondDate = expireDate.getValue();

        long duration = DAYS.between(firstDate, secondDate);
        if (duration < 0) {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setBody(new Text("Yaroqlilik muddati ishlab chiqarilgan sanadan oldin bo'lishi mumkin emas!"));
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
            JFXButton button = new JFXButton("OK");
            button.setOnAction(event -> dialog.close());
            content.setActions(button);
            dialog.show();
        }
    }

    @FXML
    void update() {
        int id = selectedDrug.getDrugID();
        Image image = productImage.getImage();
        String firstDate = manufactureDate.getValue().toString();
        String secondDate = expireDate.getValue().toString();
        int price = Integer.parseInt(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        if (DrugDAO.update(id, image, firstDate, secondDate, price, quantity)) {
            AlertShow.showAlert(Alert.AlertType.INFORMATION, "Information",
                    "Dori ma'lumotlari muvaffaqiyatli yangilandi", null);
        } else {
            AlertShow.showAlert(Alert.AlertType.WARNING, "Warning", "Ma'lumotlarni yangilashda muammo","");
        }
    }
}
