package com.wms.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class RequestInsert {
    private final int sender;
    private final int receiver;
    private final int productId;
    private final int quantity;
    private final Date timestamp;
    public RequestInsert(int sender,int productId,int quantity,Date timestamp){
        this.productId=productId;
        this.sender=sender;
        this.quantity=quantity;
        this.timestamp=timestamp;
        this.receiver=calcOptimalReceiver();
    }
    public int getSender(){
        return sender;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getReceiver() {
        return receiver;
    }

    private int calcOptimalReceiver(){
        List<Integer> allWorkers = User.getAllWOIDs();
        int minRequestsPending=Integer.MAX_VALUE;
        int availableWorker=-1;
        for(int workerId : allWorkers){
            String SQL="SELECT count(*) FROM wms.requests WHERE receiver=? AND status='PENDING'";
            try(Connection conn = DBConnection.connect()){
                PreparedStatement ps = conn.prepareStatement(SQL);
                ps.setInt(1,workerId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    if (minRequestsPending > rs.getInt(1)) {
                        minRequestsPending=rs.getInt(1);
                        availableWorker=workerId;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return availableWorker;
    }
}
