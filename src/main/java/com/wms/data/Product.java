package com.wms.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public record Product(int id, String description, int shelf, String image) {
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
                        rs.getInt("shelf"),
                        rs.getString("image")
                ));
            }
        }catch(Exception e){
            throw new Exception("Something went Wrong");
        }
        return list;
    }
    public static void insertNewProduct(Product product) throws Exception{
        String SQL="INSERT INTO wms.products (description,shelf,image) VALUES(?,?,?)";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setString(1, product.description());
            stmt.setInt(2,product.shelf());
            stmt.setString(3,product.image());
            stmt.execute();
        }catch(Exception e){
            throw new Exception("Something Went Wrong");
        }
    }
}
