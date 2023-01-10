package com.wms.data.models;

import com.wms.data.RequestSearch;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class RequestHistoryModel {
    private SimpleIntegerProperty productId;
    private SimpleIntegerProperty quantity;
    private SimpleStringProperty timestamp;
    private SimpleStringProperty status;
    public RequestHistoryModel(RequestSearch rs){
        this.productId = new SimpleIntegerProperty(rs.getProductId());
        this.quantity = new SimpleIntegerProperty(rs.getQuantity());
        this.timestamp = new SimpleStringProperty(rs.getTimestamp().toString());
        this.status = new SimpleStringProperty(rs.getRequestStatus().toString());
    }

    public int getProductId() {
        return productId.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public String getStatus() {
        return status.get();
    }

    public static List<RequestHistoryModel> getRequestHistoryModels(List<RequestSearch> list){
        List<RequestHistoryModel> models = new ArrayList<>();
        for(RequestSearch rs:list){
            models.add(new RequestHistoryModel(rs));
        }
        return models;
    }
}
