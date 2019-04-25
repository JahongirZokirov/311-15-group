package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import helpers.AlertShow;
import helpers.OpenWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Drug;
import models.DrugDAO;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

public class Add {

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
    private JFXComboBox<String> categoryBox;

    @FXML
    private JFXDatePicker manufactureDate;

    @FXML
    private JFXDatePicker expireDate;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField quantityField;

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

        String[] categories = {"Tabletka", "Antibiotik", "Maz", "Eritma", "Tomchi dori", "Vitamin", "Qo'shimcha ozuqa",
                                "Ampula", "Tabiiy dori vositasi", "Kosmetika"};
        categoryBox.getItems().addAll(categories);
    }

    @FXML
    void add() {
        Drug drug = new Drug();

        drug.setDrugName(nameField.getText());
        drug.setPrice(Integer.parseInt(priceField.getText()));
        drug.setQuantity(Integer.parseInt(quantityField.getText()));
        drug.setCategory(categoryBox.getItems().get(categoryBox.getSelectionModel().getSelectedIndex()));
        drug.setManufacturer(manufacturerField.getText());
        drug.setIllness(illnessField.getText());
        drug.setImage(productImage.getImage());
        drug.setManufactureDate(manufactureDate.getValue().toString());
        drug.setExpireDate(expireDate.getValue().toString());

        // write data to database
        if (DrugDAO.insertDrug(drug)) {
            AlertShow.showAlert(Alert.AlertType.INFORMATION, "Information", "Dori muvaffaqiyatli qo'shildi","");
        } else {
            AlertShow.showAlert(Alert.AlertType.WARNING, "Warning", "Dorini qo'shishda muammo","");
        }
    }

    @FXML
    void browsePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG", "*.JPEG");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(extensionFilterJPG, extensionFilterPNG);
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            String imagePath = null;
            try {
                imagePath = file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Image image = new Image(Objects.requireNonNull(imagePath));
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
            content.setBody(new Text("Yaroqlilik muddati ishlab chiqarilgan sanadan oldin bo'lishi mumkin emas"));
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
            JFXButton button = new JFXButton("OK");
            button.setOnAction(event -> dialog.close());
            content.setActions(button);
            dialog.show();
        }
    }

    @FXML
    void backToMain(ActionEvent actionEvent) {
        try {
            OpenWindow.openWindow(actionEvent, "views/shop.fxml", "Main",
                    1300, 680, true, "pictures/Clinic_48px.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
