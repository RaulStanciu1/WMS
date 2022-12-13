package com.wms.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RequestSearch {
    private final int id;
    private final int productId;
    private final int quantity;
    private final int sentBy;
    private final Date timestamp;
    public RequestSearch(int id, int productId,int quantity,int sentBy,Date timestamp){
        this.id=id;
        this.productId=productId;
        this.quantity=quantity;
        this.sentBy=sentBy;
        this.timestamp=timestamp;
    }
    public int getId(){
        return id;
    }
    public int getProductId(){
        return productId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSentBy() {
        return sentBy;
    }
    public static List<RequestSearch> getPendingRequestsByReceiver(int receiver) throws Exception{
        List<RequestSearch> list = new ArrayList<>();
        String SQL = "SELECT * FROM wms.requests WHERE receiver=? AND status='PENDING'";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,receiver);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new RequestSearch(
                        rs.getInt("id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getInt("sender"),
                        rs.getDate("timestamp")
                ));
            }
        }catch(Exception e){
            throw new Exception("Something Went Wrong");
        }
        return list;
    }
}
