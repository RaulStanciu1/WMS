package com.wms.data.models;

import com.wms.data.Product;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class WarehouseStockModel {
    private SimpleIntegerProperty productId;
    private SimpleStringProperty description;
    private SimpleIntegerProperty shelf;

    public WarehouseStockModel(Product p){
        this.productId = new SimpleIntegerProperty(p.id());
        this.description = new SimpleStringProperty(p.description());
        this.shelf= new SimpleIntegerProperty(p.shelf());
    }

    public int getProductId() {
        return productId.get();
    }

    public int getShelf() {
        return shelf.get();
    }

    public String getDescription() {
        return description.get();
    }

    public static List<WarehouseStockModel> getModels(List<Product> products){
        List<WarehouseStockModel> models = new ArrayList<>();
        for(Product p : products){
            models.add(new WarehouseStockModel(p));
        }
        return models;
    }
}
