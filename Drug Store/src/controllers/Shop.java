package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import helpers.AlertShow;
import helpers.Cell;
import helpers.OpenWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import models.Drug;
import models.DrugDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

public class Shop {

    @FXML
    private JFXListView<Drug> shopList;

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
    private JFXSlider amountIndicator;

    @FXML
    private Label amountLabel;

    @FXML
    private JFXDatePicker manufactureDate;

    @FXML
    private JFXDatePicker expireDate;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private JFXComboBox<String> sortBox;

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private JFXTreeTableView<Drug> tableView;
    private JFXTreeTableColumn<Drug, String> nameCol;
    private JFXTreeTableColumn<Drug, Integer> priceCol;
    private JFXTreeTableColumn<Drug, Integer> quantityCol;
    private JFXTreeTableColumn<Drug, String> categoryCol;
    private JFXTreeTableColumn<Drug, String> firstDate;
    private JFXTreeTableColumn<Drug, String> secondDate;

    @FXML
    private JFXButton drugConditionBtn;

    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXButton buyButton;

    private static Drug selectedDrug;
    private int totalPrice;
    private int amount;
    private final int NAME_SORT = 0;
    private final int PRICE_SORT = 1;
    private final int EXPIRE_DATE_SORT = 2;
    private final int CATEGORY_SORT = 3;

    @FXML
    void initialize() {
        totalPrice = 0;
        amount = 0;

        // setting comboBox
        String[] orderView = {"Nomi bo'yicha", "Narxi bo'yicha", "Yaroqlilik muddati bo'yicha",
                "Kategoriyasi bo'yicha"};
        sortBox.getItems().addAll(orderView);

        // creating tableview
        nameCol = new JFXTreeTableColumn<>("Nomi");
        nameCol.setPrefWidth(100);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().drugNameProperty());

        priceCol = new JFXTreeTableColumn<>("Narxi");
        priceCol.setPrefWidth(60);
        priceCol.setCellValueFactory(param -> param.getValue().getValue().priceProperty().asObject());

        quantityCol = new JFXTreeTableColumn<>("Miqdori");
        quantityCol.setCellValueFactory(param -> param.getValue().getValue().quantityProperty().asObject());
        quantityCol.setPrefWidth(60);

        categoryCol = new JFXTreeTableColumn<>("Kategoriya");
        categoryCol.setPrefWidth(70);
        categoryCol.setCellValueFactory(param -> param.getValue().getValue().categoryProperty());

        firstDate = new JFXTreeTableColumn<>("Ishlab chiqarilgan");
        firstDate.setPrefWidth(110);
        firstDate.setCellValueFactory(param -> param.getValue().getValue().manufactureDateProperty());

        secondDate = new JFXTreeTableColumn<>("Yaroqlilik muddati");
        secondDate.setPrefWidth(80);
        secondDate.setCellValueFactory(param -> param.getValue().getValue().expireDateProperty());
        tableView.getColumns().addAll(nameCol, priceCol, quantityCol, categoryCol, firstDate, secondDate);

        // setting values to treetabliview
        reloadTable();

        SpinnerValueFactory.IntegerSpinnerValueFactory intFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
        intFactory.valueProperty().addListener((observable, oldValue, newValue) -> {
            amountIndicator.setValue(newValue);
            amount = newValue;
            amountLabel.setText(amount + " ta");
        });

        // setting listview cell
        shopList.setExpanded(true);
        shopList.setDepth(3);
        shopList.setCellFactory(param -> new Cell());
    }

    @FXML
    void addToShopCart() {
        selectedDrug.setAmount(amount);
        selectedDrug.setSumma(amount * selectedDrug.getPrice());
        shopList.getItems().add(selectedDrug);

        totalPrice += amount * selectedDrug.getPrice();
        totalPriceLabel.setText(totalPrice + " so'm");
    }

    @FXML
    void backToMenu(Event event) {
        try {
            OpenWindow.openWindow(event, "views/identification.fxml", "Welcome", 400,
                    450, false, "pictures/Pill_96px.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buy() {
        ObservableList<Drug> buyedDrugs = shopList.getItems();
        DrugDAO.updateAmountBoughtParts(buyedDrugs, totalPrice);

        AlertShow.showAlert(Alert.AlertType.INFORMATION, "Harid", null, "Tanlangan mahsulotlar sotildi");

        totalPrice = 0;
        amount = 0;
        shopList.getItems().clear();
        amountIndicator.setValue(0);
        amountIndicator.setMax(0);
        amountLabel.setText("0");
        totalPriceLabel.setText("0");

        reloadTable();

        SpinnerValueFactory.IntegerSpinnerValueFactory intFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
        intFactory.setValue(0);
    }

    @FXML
    void cancel() {
        Drug cancelledDrug = shopList.getSelectionModel().getSelectedItem();
        if (cancelledDrug != null) {
            totalPrice -= cancelledDrug.getAmount() * cancelledDrug.getPrice();
            totalPriceLabel.setText(totalPrice + " so'm");
            shopList.getItems().remove(shopList.getSelectionModel().getSelectedItem());
        } else {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setBody(new Text("Qaytarmoqchi bo'lgan mahsulotni tanlang"));
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
            JFXButton button = new JFXButton("OK");
            button.setOnAction(event -> dialog.close());
            content.setActions(button);
            dialog.show();
        }
    }

    @FXML
    void clearCart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Tasdiqlash");
        alert.setContentText("Savatni tozalashni tasdiqlaysizmi?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            shopList.getItems().clear();
            totalPrice = 0;
            amount = 0;
            totalPriceLabel.setText("0");
        }
    }

    @FXML
    void searchItems() {
        String search = searchField.getText().toLowerCase();
        tableView.setPredicate(searchItem -> searchItem.getValue().getDrugName().toLowerCase().contains(search)
                || searchItem.getValue().getIllness().toLowerCase().contains(search));
    }

    @FXML
    void setPurchaseAmount() {
        amount = (int) Math.floor(amountIndicator.getValue());
        amountLabel.setText(amount + " ta");
        SpinnerValueFactory.IntegerSpinnerValueFactory intFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
        intFactory.setValue(amount);
    }

    @FXML
    void showConditionMessage() {
        if (!drugConditionBtn.getStylesheets().isEmpty()) {
            String condition = drugConditionBtn.getStylesheets().get(0);
            switch (condition) {
                case "StyleSheets/greenStyle.css":
                    AlertShow.showAlert(Alert.AlertType.INFORMATION, "Information", "Dori holati",
                            "Ushbu dorining saqlash muddati yaxshi holatda");
                    break;
                case "StyleSheets/yellowStyle.css":
                    AlertShow.showAlert(Alert.AlertType.WARNING, "Warning", "Dori holati",
                            "Ushbu dorining saqlash muddati tugashiga 3 oydan kam qolgan!");
                    break;
                case "StyleSheets/redStyle.css":
                    AlertShow.showAlert(Alert.AlertType.WARNING, "Warning", "Dori holati",
                            "Ushbu dorining saqlash muddati tugagan!\n " +
                                    "Uni sota olmaysiz. Iltimos uni bazadan o'chiring");
                    break;
            }
        }
    }

    @FXML
    void selectDrug() {
        int index = tableView.getSelectionModel().getSelectedIndex();
        selectedDrug = tableView.getTreeItem(index).getValue();

        // convert dates to Localdates
        LocalDate madeDate = LocalDate.parse(selectedDrug.getManufactureDate());
        LocalDate expDate = LocalDate.parse(selectedDrug.getExpireDate());

        // setting first styles
        tableView.getStylesheets().clear();
        tableView.getStylesheets().add("StyleSheets/greenStyle.css");
        drugConditionBtn.getStylesheets().clear();
        drugConditionBtn.getStylesheets().add("StyleSheets/greenStyle.css");
        buyButton.setDisable(false);

        // setting drug condition
        LocalDate now = LocalDate.now();
        long remainingDays = DAYS.between(now, expDate);
        long remainingMonths = MONTHS.between(now, expDate);
        if (remainingDays < 0) {
            tableView.getStylesheets().clear();
            tableView.getStylesheets().add("StyleSheets/redStyle.css");
            drugConditionBtn.getStylesheets().clear();
            drugConditionBtn.getStylesheets().add("StyleSheets/redStyle.css");
            buyButton.setDisable(true);
        } else if (remainingMonths < 3) {
            tableView.getStylesheets().clear();
            tableView.getStylesheets().add("StyleSheets/yellowStyle.css");
            drugConditionBtn.getStylesheets().clear();
            drugConditionBtn.getStylesheets().add("StyleSheets/yellowStyle.css");
        }

        // setting max value to amount indicator and spinner
        amountIndicator.setMax(selectedDrug.getQuantity());
        SpinnerValueFactory.IntegerSpinnerValueFactory intFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
        intFactory.setMax(selectedDrug.getQuantity());

        // setting data to window
        nameField.setText(selectedDrug.getDrugName());
        priceField.setText(selectedDrug.getPrice() + " so'm");
        illnessField.setText(selectedDrug.getIllness());
        manufacturerField.setText(selectedDrug.getManufacturer());
        productImage.setImage(selectedDrug.getImage());
        manufactureDate.setValue(madeDate);
        expireDate.setValue(expDate);
    }

    @FXML
    void sortBy() {
        tableView.getSortOrder().clear();
        int selectedIndex = sortBox.getSelectionModel().getSelectedIndex();
        switch (selectedIndex) {
            case NAME_SORT:
                tableView.getSortOrder().add(nameCol);
                break;
            case PRICE_SORT:
                tableView.getSortOrder().add(priceCol);
                break;
            case EXPIRE_DATE_SORT:
                tableView.getSortOrder().add(secondDate);
                break;
            case CATEGORY_SORT:
                tableView.getSortOrder().add(categoryCol);
                break;
        }
    }

    @FXML
    void addNewDrug(ActionEvent actionEvent) {
        try {
            OpenWindow.openWindowMenuItem(amountLabel, "views/add.fxml", "Yangi dori", 482, 600,
                    false, "resources/pictures/Pill_96px.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteDrug() {
        if (selectedDrug == null) {
            AlertShow.showAlert(Alert.AlertType.WARNING, "Warning", "Dori tanlanmadi",
                    "Jadvaldan ma'lumoti o'zgartirilishi kerak bo'lgan dorini tanlang");
            return;
        }

        int ID = selectedDrug.getDrugID();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Tanlang");
        alert.setContentText("Nolga tushirishda mahsulot soni nolga tenglashtiriladi va haridor uchun ko'rinmaydi\n" +
                "Keyinchalik bu mahsulot malumotlari yangilanishi mumkin");

        ButtonType buttonTypeOne = new ButtonType("Nolga tushirish");
        ButtonType buttonTypeTwo = new ButtonType("Butunlay o'chirish");
        ButtonType buttonTypeCancel = new ButtonType("Bekor qilish", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        boolean success = false;
        if (result.get() == buttonTypeOne) {
            success = DrugDAO.updateToZero(ID);
        } else if (result.get() == buttonTypeTwo) {
            success = DrugDAO.deleteItem(ID);
        }

        if (success) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Information");
            alert1.setHeaderText("O'chirish muvaffaqiyatli amalga oshirildi");
            alert1.showAndWait();
        }

        reloadTable();
    }

    @FXML
    void updateDrug(ActionEvent actionEvent) {
        if (selectedDrug == null) {
            AlertShow.showAlert(Alert.AlertType.WARNING, "Warning", "Dori tanlanmadi",
                    "Jadvaldan ma'lumoti o'zgartirilishi kerak bo'lgan dorini tanlang");
            return;
        }
        try {
            OpenWindow.openWindowMenuItem(amountLabel, "views/update.fxml", "Yangi dori", 482, 600,
                    false, "resources/pictures/Pill_96px.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reloadTable() {
        List<Drug> drugList = DrugDAO.queryAllDrugs();
        ObservableList<Drug> drugs = FXCollections.observableArrayList(drugList);
        final TreeItem<Drug> root = new RecursiveTreeItem<>(drugs, RecursiveTreeObject::getChildren);
        tableView.setRoot(root);
        tableView.setShowRoot(false);
    }

    static Drug getSelectedDrug() {
        return selectedDrug;
    }
}
