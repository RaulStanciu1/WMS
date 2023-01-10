package com.wms.data.misuse;

import com.wms.data.DBConnection;
import com.wms.data.Status;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutomatedMisuseAnalysis {
    private LocalDate analysisDate;
    private List<Request> approvedRequests;
    private List<Request> deniedRequests;
    private List<Request> pendingRequests;
    private List<Request> allRequests;
    private List<Misuse> currentInconsistencies;
    public AutomatedMisuseAnalysis(){}


    /**
     * 1. Setting Up
     */
    public void setupAnalysis(LocalDate date){
        this.analysisDate = date;
        this.approvedRequests = new ArrayList<>();
        this.deniedRequests = new ArrayList<>();
        this.pendingRequests = new ArrayList<>();
        this.allRequests = new ArrayList<>();
        this.currentInconsistencies = new ArrayList<>();
    }

    /**
     * 2. Gathering Relevant Information From the database
     */
    public void gatherInfoFromDB() throws Exception{
        String SQL_APPROVED = "SELECT * FROM wms.requests WHERE year(timestamp)= ? AND month(timestamp)=? and day(timestamp)=? and status='APPROVED'";
        String SQL_DENIED = "SELECT * FROM wms.requests WHERE year(timestamp)= ? AND month(timestamp)=? and day(timestamp)=? and status='DENIED'";
        String SQL_PENDING = "SELECT * FROM wms.requests WHERE year(timestamp)= ? AND month(timestamp)=? and day(timestamp)=? and status='PENDING'";
        try{
            try(Connection conn = DBConnection.connect()){
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement(SQL_APPROVED);
                stmt.setInt(1,this.analysisDate.getYear());
                stmt.setInt(2,this.analysisDate.getMonthValue());
                stmt.setInt(3,this.analysisDate.getDayOfMonth());
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    this.approvedRequests.add(
                            new Request(
                                    rs.getInt("id"),
                                    rs.getInt("sender"),
                                    rs.getInt("receiver"),
                                    rs.getInt("product_id"),
                                    rs.getInt("quantity"),
                                    rs.getDate("timestamp").toLocalDate(),
                                    Status.APPROVED
                            )
                    );
                }
                stmt = conn.prepareStatement(SQL_DENIED);
                stmt.setInt(1,this.analysisDate.getYear());
                stmt.setInt(2,this.analysisDate.getMonthValue());
                stmt.setInt(3,this.analysisDate.getDayOfMonth());
                rs = stmt.executeQuery();
                while(rs.next()){
                    this.deniedRequests.add(
                            new Request(
                                    rs.getInt("id"),
                                    rs.getInt("sender"),
                                    rs.getInt("receiver"),
                                    rs.getInt("product_id"),
                                    rs.getInt("quantity"),
                                    rs.getDate("timestamp").toLocalDate(),
                                    Status.DENIED
                            )
                    );
                }

                stmt = conn.prepareStatement(SQL_PENDING);
                stmt.setInt(1,this.analysisDate.getYear());
                stmt.setInt(2,this.analysisDate.getMonthValue());
                stmt.setInt(3,this.analysisDate.getDayOfMonth());
                rs = stmt.executeQuery();
                while(rs.next()){
                    this.pendingRequests.add(
                            new Request(
                                    rs.getInt("id"),
                                    rs.getInt("sender"),
                                    rs.getInt("receiver"),
                                    rs.getInt("product_id"),
                                    rs.getInt("quantity"),
                                    rs.getDate("timestamp").toLocalDate(),
                                    Status.PENDING
                            )
                    );
                }
                this.allRequests.addAll(this.pendingRequests);
                this.allRequests.addAll(this.deniedRequests);
                this.allRequests.addAll(this.approvedRequests);

                conn.commit();
                conn.setAutoCommit(true);
                rs.close();
                stmt.close();
            }
        }catch(Exception e){
            System.out.println(e);
            throw new Exception("Error: Could not gather necessary information from the database");
        }
    }

    /**
     * 3. Reviewing information to check for possible requesting anomalies(possible spam)
     */
    public void checkForSpam(){
        Set<Integer> operatorsWhoSentRequests = new HashSet<>();
        for(Request r:this.allRequests){
            operatorsWhoSentRequests.add(r.sender());
        }

        for(int operatorId:operatorsWhoSentRequests){
            // Check if there are too many requests sent in a day
            int numberOfRequestsMade=0;
            for(Request r:this.allRequests){
                if(r.sender()==operatorId){
                    numberOfRequestsMade++;
                }
            }
            if(numberOfRequestsMade>10){
                this.currentInconsistencies.add(new Misuse(operatorId,
                        "Possible misuse of Requesting System: Too many requests made in a day by the same PO",
                        MisuseType.TOO_MANY_REQ));
            }
            int productIdFreq=0;
            for(Request r:this.allRequests){

                for(Request r2:this.allRequests){
                    if(r.sender()==operatorId && r.productId()==r2.productId()) productIdFreq++;
                }

            }
            if(productIdFreq>3){
                this.currentInconsistencies.add(new Misuse(operatorId,
                        "Possible misuse of Requesting System: PO made too many similar requests in a day",
                        MisuseType.SPAM));
            }
        }
    }
    /**
     * 4. Reviewing information to check for possible requesting anomalies(invalid input)
     */
    public void checkForInvalidInput(){
        for(Request r:this.allRequests){
            if(r.quantity()<5){
                this.currentInconsistencies.add(new Misuse(r.sender(),
                        "Possible misuse of Requesting System: PO made a request with abnormally low quantity"
                                ,MisuseType.LOW_Q)
                        );
            }
            if(r.quantity()>500){
                this.currentInconsistencies.add(new Misuse(r.sender(),
                        "Possible misuse of Requesting System: PO made a request with abnormally large quantity",
                        MisuseType.HIGH_Q));
            }
        }
    }

    /**
     * 5. Checking for possible employee inactivity
     */
    public void checkForInactivity(){
        if(this.allRequests.size()==0){
            this.currentInconsistencies.add(
                    new Misuse(7,"No Requests have been made yet!",MisuseType.NO_REQUESTS)
            );
            return;
        }
        Set<Integer> warehouseOp = new HashSet<>();
        for(Request r:this.pendingRequests){
            warehouseOp.add(r.receiver());
        }
        for(int woId:warehouseOp){
            int pending=0;
            for(Request r:this.pendingRequests){
                if(r.receiver()==woId){
                    pending++;
                }
            }
            if(pending>10){
                this.currentInconsistencies.add(
                        new Misuse(woId,"Inactivity Warehouse Operator: WO has too many pending requests left unchecked",
                                MisuseType.INACTIVITY)
                );
            }
        }
    }
    /**
     * 6. Checking for possible employee malicious intent
     */
    public void checkForMaliciousIntent(){
        Set<Integer> warehouseOp = new HashSet<>();
        for(Request r:this.pendingRequests){
            warehouseOp.add(r.receiver());
        }
        for(int opId:warehouseOp){
            int nrOfDeniedRequests=0;
            for(Request r:this.deniedRequests){
                if(r.receiver()==opId && r.quantity()>=5 && r.quantity()<=500){
                    nrOfDeniedRequests++;
                }
            }
            if(nrOfDeniedRequests>3){
                this.currentInconsistencies.add(
                        new Misuse(opId,
                                "Possible Malicious Intent: WO has denied too many valid requests",
                                MisuseType.MALICIOUS)
                );
            }
        }
    }
    /**
     * 7. Reviewing found inconsistencies(Creating a report) and possible actions to take
     */
    public Report reviewingInconsistencies(){
        List<String> recommendedActions = new ArrayList<>();
        for(Misuse m:this.currentInconsistencies){
            switch(m.type()){
                case SPAM -> {
                    recommendedActions.add("Remove The Production Operator's Account");
                }
                case LOW_Q,HIGH_Q -> {
                    recommendedActions.add("Send a Warning to the Production Operator");
                }
                case MALICIOUS -> {
                    recommendedActions.add("Remove The Warehouse Operator's Account and Fine for Possible Damages");
                }
                case INACTIVITY -> {
                    recommendedActions.add("Send a Warning to the Warehouse Operator");
                }
                case TOO_MANY_REQ -> {
                    recommendedActions.add("Make sure the requests are genuine");
                }
                case NO_REQUESTS -> {
                    recommendedActions.add("Try to run the AMA when there is Warehouse Activity");
                }
            }
        }
        return new Report(this.currentInconsistencies,recommendedActions,this.analysisDate);
    }

    /**
     * 8. Logging inconsistencies to the database
     */
    public void logInconsistencies(Report report) throws Exception{
        String SQL = "INSERT INTO wms.reports (culprit,details,timestamp,actions) VALUES(?,?,?,?)";
        try(Connection conn = DBConnection.connect()){
            conn.setAutoCommit(false);
            PreparedStatement ps;
            for(int i=0;i<report.getInconsistencies().size();i++){
                ps = conn.prepareStatement(SQL);
                ps.setInt(1,report.getInconsistencies().get(i).culprit());
                ps.setString(2,report.getInconsistencies().get(i).details());
                ps.setDate(3, Date.valueOf(report.getDate()));
                ps.setString(4,report.getPossibleActions().get(i));
                ps.execute();
            }
            conn.commit();
            conn.setAutoCommit(true);
        }catch(Exception e){
            System.out.println(e);
            throw new Exception("Couldn't log report to db");
        }
    }

}
