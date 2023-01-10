package com.wms.controller;

import com.wms.data.misuse.AutomatedMisuseAnalysis;
import com.wms.data.misuse.Report;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.time.LocalDate;

public class ViewReportsController implements Controller{
    @FXML private DatePicker reportDate;
    @FXML private TextArea reportsInfo;
    @FXML private Button beginAnalysisBtn;
    @FXML private TextArea reportDetails;

    @Override
    public void init() {
        reportDate.setValue(LocalDate.now());
    }

    private void appendNewLine(String msg){
        reportsInfo.appendText("NOTE:   "+msg+"\n");

    }
    public void startAutomatedMisuseAnalysis(){
        beginAnalysisBtn.setDisable(true);
        LocalDate date = reportDate.getValue();
        if(date==null){
            reportsInfo.setText("FATAL ERROR:NO DATE INPUT");
            beginAnalysisBtn.setDisable(false);
            return;
        }
        AutomatedMisuseAnalysis analysis = new AutomatedMisuseAnalysis();
        reportsInfo.setText("Beginning Analysis...\n");
        appendNewLine("*");
        appendNewLine("*");
        appendNewLine("*");
        appendNewLine("1.Setting up Analysis Data");
        analysis.setupAnalysis(date);
        appendNewLine("Setting up was successful...");
        appendNewLine("*");
        appendNewLine("*");
        appendNewLine("*");
        appendNewLine("2.Retrieving Necessary Info from the database");
        try{
            analysis.gatherInfoFromDB();
            appendNewLine("Gathering Information was successful...");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("3.Reviewing information to check for possible requesting anomalies(possible spam)");
            analysis.checkForSpam();
            appendNewLine("Information has been reviewed...");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("4.Reviewing information to check for possible requesting anomalies(invalid input)");
            analysis.checkForInvalidInput();
            appendNewLine("Information has been reviewed...");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("5. Checking for possible employee inactivity");
            analysis.checkForInactivity();
            appendNewLine("Checking completed...");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("6.Checking for possible employee malicious intent");
            analysis.checkForMaliciousIntent();
            appendNewLine("Checking completed...");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("7.Reviewing found inconsistencies(Creating a report) and possible actions to take");
            Report finalReport=analysis.reviewingInconsistencies();
            appendNewLine("Report Created...");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("8.Logging inconsistencies to the database");
            analysis.logInconsistencies(finalReport);
            appendNewLine("Information has been logged");
            String tmp = finalReport.getInconsistencies().size()>0?"INCONSISTENCIES FOUND":"NO INCONSISTENCIES FOUND";
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("*");
            appendNewLine("REPORT STATUS: "+tmp);
            reportDetails.setText(finalReport.toString());
            beginAnalysisBtn.setDisable(false);
        }catch(Exception e){
            reportDetails.setText("FATAL ERROR: COULD NOT COMPLETE ANALYSIS\n"+e.getMessage());
            beginAnalysisBtn.setDisable(false);
        }
    }
}
