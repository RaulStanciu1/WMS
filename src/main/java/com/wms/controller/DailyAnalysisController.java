package com.wms.controller;

import com.wms.data.analysis.DailyAnalysis;
import com.wms.data.analysis.DailyReport;
import com.wms.data.analysis.PerformanceMeasurement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class DailyAnalysisController implements Controller{
    @FXML PieChart dailyStats;
    @FXML Label recommendedActions;
    private void errorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR,msg, ButtonType.OK);
        alert.showAndWait();
    }
    @Override
    public void init() {
        try {
            dailyStats.setStyle("-fx-font: 12px \"Century Gothic\";");
            DailyAnalysis analysis=PerformanceMeasurement.getDailyData();
            DailyReport report=analysis.getReport();
            report.analyzeReport();
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                    new PieChart.Data("Denied Requests", report.getDeniedRequestsPercentage()),
                    new PieChart.Data("Pending Requests", report.getPendingRequestsPercentage()),
                    new PieChart.Data("Approved Requests", report.getApprovedRequestsPercentage())
            );
            dailyStats.getData().addAll(data);
            recommendedActions.setText(report.getRecommendedActions());
        }catch(Exception e){
            errorMessage(e.getMessage());
        }
    }

}
