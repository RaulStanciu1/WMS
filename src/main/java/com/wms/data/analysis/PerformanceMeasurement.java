package com.wms.data.analysis;

import com.wms.data.DBConnection;

import java.sql.*;
import java.util.Calendar;

public class PerformanceMeasurement {
    private static int getApprovedRequests(int day,int month,int year) throws Exception{
        int data=0;
        String SQL="CALL wms.get_requests_by_date('APPROVED',?,?,?)";
        try(Connection conn = DBConnection.connect()){
            CallableStatement cs = conn.prepareCall(SQL);
            cs.setInt(1,day);
            cs.setInt(2,month);
            cs.setInt(3,year);
            ResultSet rs = cs.executeQuery();
            if(rs.next()){
                data=rs.getInt(1);
            }
        }catch(Exception e){
            System.out.println(e);
            throw new Exception("Something Went Wrong");
        }
        return data;
    }
    private static int getDeniedRequests(int day,int month,int year) throws Exception{
        int data=0;
        String SQL="CALL wms.get_requests_by_date('DENIED',?,?,?)";
        try(Connection conn = DBConnection.connect()){
            CallableStatement cs = conn.prepareCall(SQL);
            cs.setInt(1,day);
            cs.setInt(2,month);
            cs.setInt(3,year);
            ResultSet rs = cs.executeQuery();
            if(rs.next()){
                data=rs.getInt(1);
            }
        }catch(Exception e){
            System.out.println(e);
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
    public static DailyAnalysis getDailyData() throws Exception{
        return new DailyAnalysis();
    }
}
