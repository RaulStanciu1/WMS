package com.wms.data.models;

import com.wms.data.RequestSearch;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class WoRequestModel {
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty productId;
    private final SimpleIntegerProperty quantity;
    private final SimpleIntegerProperty sentBy;
    private final SimpleStringProperty timestamp;
    public WoRequestModel(int id, int productId,int quantity,int sentBy, Date timestamp){
        this.productId = new SimpleIntegerProperty(productId);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.sentBy = new SimpleIntegerProperty(sentBy);
        this.timestamp = new SimpleStringProperty(timestamp.toString());
        this.id= new SimpleIntegerProperty(id);
    }

    public WoRequestModel(RequestSearch rs){
        this.id=new SimpleIntegerProperty(rs.getId());
        this.productId=new SimpleIntegerProperty(rs.getProductId());
        this.quantity=new SimpleIntegerProperty(rs.getQuantity());
        this.sentBy=new SimpleIntegerProperty(rs.getSentBy());
        this.timestamp=new SimpleStringProperty(rs.getTimestamp().toString());
    }
    public int getId(){
        return id.get();
    }
    public int getProductId(){
        return productId.get();
    }

    public int getQuantity(){
        return quantity.get();
    }

    public int getSentBy(){
        return sentBy.get();
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public static List<WoRequestModel> modelList(List<RequestSearch> l){
        List<WoRequestModel> list = new ArrayList<>();
        for(RequestSearch rs:l){
            list.add(new WoRequestModel(rs));
        }
        return list;
    }
}
