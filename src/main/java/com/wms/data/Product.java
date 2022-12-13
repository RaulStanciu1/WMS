package com.wms.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String description;
    private int shelf;
    public Product(int id, String description, int shelf){
        this.id=id;
        this.description=description;
        this.shelf=shelf;
    }
    public int getId(){
        return id;
    }
    public static List<Integer> getProductIds(List<Product> list){
        List<Integer> integerList = new ArrayList<>();
        for(Product p: list){
            integerList.add(p.getId());
        }
        return integerList;
    }
    public static List<Product> getProductsFromDB() throws Exception{
        List<Product> list = new ArrayList<>();
        String SQL="Select * From wms.products";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getInt("shelf")
                ));
            }
        }catch(Exception e){
            throw new Exception("Something went Wrong");
        }
        return list;
    }
}
