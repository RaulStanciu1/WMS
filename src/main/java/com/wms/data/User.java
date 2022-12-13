package com.wms.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    public String getUsername(){
        return username;
    }
    public Position getPosition(){
        return position;
    }
    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }
    public String toString(){
        return id+" "+username+" "+name+" "+position;
    }
    public static List<Integer> getAllWOIDs(){
        List<Integer> ids = new ArrayList<>();
        String SQL="SELECT id from wms.users WHERE position='WAREHOUSE_OP' AND status='APPROVED'";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ids.add(rs.getInt("id"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ids;
    }

    public static List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String SQL="SELECT * from wms.users WHERE status='APPROVED' and position !='ADMIN'";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("display_name"),
                        Position.valueOf(rs.getString("position"))
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public static List<User> getAllPendingUsers(){
        List<User> users = new ArrayList<>();
        String SQL="SELECT * from wms.users WHERE status='PENDING'";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("display_name"),
                        Position.valueOf(rs.getString("position"))
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return users;
    }

}
