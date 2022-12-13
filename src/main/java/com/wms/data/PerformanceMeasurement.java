package com.wms.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class PerformanceMeasurement {
    private static int getApprovedRequests(int day,int month,int year) throws Exception{
        int data=0;
        String SQL="SELECT count(*) as data from wms.requests WHERE MONTH(timestamp)=? and DAY(timestamp)=? and YEAR(timestamp)=? and status='APPROVED'";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,month);
            ps.setInt(2,day);
            ps.setInt(3,year);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                data=rs.getInt("data");
            }
        }catch(Exception e){
            throw new Exception("Something Went Wrong");
        }
        return data;
    }
    private static int getDeniedRequests(int day,int month,int year) throws Exception{
        int data=0;
        String SQL="SELECT count(*) as data from wms.requests WHERE MONTH(timestamp)=? and DAY(timestamp)=? and YEAR(timestamp)=? and status='DENIED'";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,month);
            ps.setInt(2,day);
            ps.setInt(3,year);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                data=rs.getInt("data");
            }
        }catch(Exception e){
            throw new Exception("Something Went Wrong");
        }
        return data;
    }
    public static int[] getMonthlyData(Date currDate) throws Exception {
        int[] data = new int[31];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        int currIndex;
        for(int i=0;i<31;++i){
            currIndex=0;
            currIndex+=getApprovedRequests(i+1,month,year);
            currIndex-=getDeniedRequests(i+1,month,year);
            data[i]=currIndex;
        }
        return data;
    }
}
