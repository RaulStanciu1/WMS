package com.wms.data;

public class User {
    private final int id;
    private final String username;
    private final String name;
    private final Position position;
    public User(int id, String username, String name, Position position){
        this.id=id;
        this.username=username;
        this.name=name;
        this.position=position;
    }
    public Position getPosition(){
        return position;
    }
    public String toString(){
        return id+" "+username+" "+name+" "+position;
    }
}
