package helpers;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Drug;


public class Cell extends ListCell<Drug> {
    private HBox hBox;
    private Label amountLabel, priceLabel, name;
    private ImageView productImage;

    public Cell() {
        super();
        this.hBox = new HBox();
        this.hBox.setMaxWidth(300);

        this.amountLabel = new Label();
        this.amountLabel.setFont(new Font("Arial", 16));

        this.priceLabel = new Label();
        this.priceLabel.setFont(new Font("Arial", 16));

        this.name = new Label();
        this.name.setFont(new Font("Arial", 16));
        this.name.setMaxWidth(120);
        this.name.setTextFill(Color.web("#0076a3"));

        Pane pane = new Pane();

        this.productImage = new ImageView();
        this.productImage.setFitWidth(50);
        this.productImage.setFitHeight(60);


        this.hBox.getChildren().addAll(productImage, name, amountLabel, priceLabel);
        HBox.setHgrow(pane, Priority.ALWAYS);
        HBox.setMargin(productImage, new Insets(0, 0, 0, 10));
        HBox.setMargin(name, new Insets(20, 0, 0, 10));
        HBox.setMargin(amountLabel, new Insets(20, 0, 0, 10));
        HBox.setMargin(priceLabel, new Insets(20, 0, 0, 10));
    }

    @Override
    protected void updateItem(Drug drug, boolean empty) {
        super.updateItem(drug, empty);
        setText(null);
        setGraphic(null);

        if (drug != null && !empty) {
            name.setText(drug.getDrugName());
            amountLabel.setText(drug.getAmount() + " ta");
            priceLabel.setText(drug.getSumma() + "");
            productImage.setImage(drug.getImage());
            setGraphic(hBox);
        }
    }
}