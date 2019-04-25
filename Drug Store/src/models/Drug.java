package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Drug extends RecursiveTreeObject<Drug> {
    private IntegerProperty drugID;
    private StringProperty drugName;
    private IntegerProperty price;
    private IntegerProperty quantity;
    private StringProperty manufactureDate;
    private StringProperty expireDate;
    private StringProperty category;
    private StringProperty illness;
    private StringProperty manufacturer;
    private Image image;
    private int amount;
    private int summa;

    public Drug() {
        this.drugID = new SimpleIntegerProperty();
        this.drugName = new SimpleStringProperty();
        this.price = new SimpleIntegerProperty();
        this.quantity = new SimpleIntegerProperty();
        this.manufactureDate = new SimpleStringProperty();
        this.expireDate = new SimpleStringProperty();
        this.category = new SimpleStringProperty();
        this.illness = new SimpleStringProperty();
        this.manufacturer = new SimpleStringProperty();
    }

    public int getDrugID() {
        return drugID.get();
    }

    public IntegerProperty drugIDProperty() {
        return drugID;
    }

    public void setDrugID(int drugID) {
        this.drugID.set(drugID);
    }

    public String getDrugName() {
        return drugName.get();
    }

    public StringProperty drugNameProperty() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName.set(drugName);
    }

    public int getPrice() {
        return price.get();
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public String getManufactureDate() {
        return manufactureDate.get();
    }

    public StringProperty manufactureDateProperty() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate.set(manufactureDate);
    }

    public String getExpireDate() {
        return expireDate.get();
    }

    public StringProperty expireDateProperty() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate.set(expireDate);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getIllness() {
        return illness.get();
    }

    public StringProperty illnessProperty() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness.set(illness);
    }

    public String getManufacturer() {
        return manufacturer.get();
    }

    public StringProperty manufacturerProperty() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setSumma(int summa) {
        this.summa = summa;
    }

    public int getSumma() {
        return summa;
    }
}
