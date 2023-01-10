package com.wms.controller;

import com.wms.data.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.util.Random;

public class NewProductController implements Controller{
    private FileChooser fileChooser;
    private Stage stage;
    @FXML private Button uploadBtn;
    @FXML Label filePath;
    @FXML TextArea productDescription;
    private void errorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
    @Override
    public void init() {
        fileChooser = new FileChooser();
        stage = (Stage) uploadBtn.getScene().getWindow();
    }
    public void uploadImage(){
        File image = fileChooser.showOpenDialog(stage);
        if(image!=null){
            try{
                String mimetype = Files.probeContentType(image.toPath());
                if (mimetype != null && mimetype.split("/")[0].equals("image")) {
                    filePath.setText(image.getAbsolutePath());

                }
            }catch(Exception e){
                errorMessage(e.getMessage());
            }

        }
    }
    public void createNewProduct(){
        try {
            if (productDescription.getText().isEmpty()) {
                throw new Exception("Product Description is Mandatory");
            }
            Random random = new Random();
            int shelfNumber = random.nextInt(9) + 1;
            String description = productDescription.getText();
            String imagePath = filePath.getText();
            Product tmpProduct = new Product(0, description, shelfNumber, imagePath);
            Product.insertNewProduct(tmpProduct);
            filePath.setText("No File Yet...");
            productDescription.setText("");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Product Has Been Created",ButtonType.OK);
            alert.showAndWait();
        }catch(Exception e){
            errorMessage(e.getMessage());
        }
    }
}
