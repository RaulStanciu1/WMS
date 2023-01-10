package com.wms.data.analysis;

import com.wms.data.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DailyAnalysis {
    private int totalRequestsMade;
    private int deniedRequests;
    private int approvedRequests;
    private int pendingRequests;
    public DailyAnalysis() throws Exception{
        String SQL="SELECT * FROM wms.daily_statistics";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                this.approvedRequests=rs.getInt("Approved Requests");
                this.deniedRequests=rs.getInt("Denied Requests");
                this.pendingRequests=rs.getInt("Pending Requests");
                this.totalRequestsMade=rs.getInt("Total Requests");
            }
        }catch(Exception e){
            throw new Exception("Something Went Wrong");
        }
    }
    public DailyReport getReport(){
        float totalPercentage = totalRequestsMade;
        float approvedPercentage=0;
        float deniedPercentage=0;
        float pendingPercentage=0;
        if(totalPercentage!=0) {
            approvedPercentage = approvedRequests / totalPercentage * 100;
            deniedPercentage = deniedRequests / totalPercentage * 100;
            pendingPercentage = pendingRequests / totalPercentage * 100;
        }
        DailyReport report = new DailyReport(deniedPercentage,approvedPercentage,pendingPercentage);
        report.analyzeReport();
        return report;
    }

    public int getTotalRequestsMade() {
        return totalRequestsMade;
    }

    public int getApprovedRequests() {
        return approvedRequests;
    }

    public int getDeniedRequests() {
        return deniedRequests;
    }

    public int getPendingRequests() {
        return pendingRequests;
    }
}
