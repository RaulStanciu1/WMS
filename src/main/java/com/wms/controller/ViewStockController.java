package com.wms.controller;

import com.wms.data.Product;
import com.wms.data.models.WarehouseStockModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ViewStockController implements Controller{
    @FXML private TableView<WarehouseStockModel> stockTable;
    @FXML private TableColumn<WarehouseStockModel,String> productIdColumn;
    @FXML private TableColumn<WarehouseStockModel,String> descriptionColumn;
    @FXML private TableColumn<WarehouseStockModel,String> shelfColumn;
    private void errorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR,msg,ButtonType.OK);
        alert.showAndWait();
    }
    private void fillStockTable() throws Exception{
        try{
            List<Product> tmpList = Product.getProductsFromDB();
            List<WarehouseStockModel> models = WarehouseStockModel.getModels(tmpList);
            ObservableList<WarehouseStockModel> stock = FXCollections.observableList(models);
            productIdColumn.setCellValueFactory(new PropertyValueFactory<>("ProductId"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
            shelfColumn.setCellValueFactory(new PropertyValueFactory<>("Shelf"));
            stockTable.setItems(stock);
        }catch(Exception e){
            throw new Exception("Something Went Wrong On Our End! Please Try Again Later");
        }

    }
    @Override
    public void init() {
        try{
            stockTable.setStyle("-fx-font: 12px \"Century Gothic\";");
            fillStockTable();
        }catch(Exception e){
            errorMessage(e.getMessage());
        }

    }

}
