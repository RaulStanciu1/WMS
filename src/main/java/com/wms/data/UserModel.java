package com.wms.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty name;
    private final SimpleStringProperty position;
    public UserModel(User u){
        this.id = new SimpleIntegerProperty(u.getId());
        this.username = new SimpleStringProperty(u.getUsername());
        this.name = new SimpleStringProperty(u.getName());
        this.position = new SimpleStringProperty(String.valueOf(u.getPosition()));
    }

    public int getId(){
        return id.get();
    }
    public String getUsername(){
        return username.get();
    }
    public String getName(){
        return name.get();
    }
    public String getPosition(){
        return position.get();
    }

    public static List<UserModel>listModel(List<User> l){
        List<UserModel> list = new ArrayList<>();
        for(User u:l){
            list.add(new UserModel(u));
        }
        return list;
    }
}
