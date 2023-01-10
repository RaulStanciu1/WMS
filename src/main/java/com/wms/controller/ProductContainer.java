package com.wms.controller;

import com.wms.data.Product;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;

public class ProductContainer extends Region {
    private VBox pane;
    private Product userData;
    private Label productDescription;
    private Label productId;
    private ImageView productImage;
    public ProductContainer(Product product) throws Exception{
        this.pane=new VBox();
        this.pane.alignmentProperty().setValue(Pos.CENTER);
        this.userData=product;
        this.productDescription=new Label();
        this.productDescription.setText(userData.description());
        this.productDescription.setStyle("-fx-font: 20px \"Century Gothic\";");
        this.productId=new Label();
        this.productId.setText(String.valueOf(userData.id()));
        this.productId.setStyle("-fx-font: 20px \"Century Gothic\";");
        this.productImage = new ImageView();
        this.productImage.setImage(new Image(new FileInputStream(product.image())));
        this.productImage.setFitHeight(200);
        this.productImage.setFitWidth(200);
        this.productImage.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);-fx-background-radius: 5;");
        pane.getChildren().addAll(productId,productImage,productDescription);
    }

    public VBox getPane() {
        return pane;
    }
}
