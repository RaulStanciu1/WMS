package com.wms.controller;

import com.wms.Main;
import com.wms.data.PerformanceMeasurement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Date;

public class AdminController implements Controller{
    @FXML private NumberAxis xAxis;
    @FXML private CategoryAxis yAxis;
    @FXML private LineChart<String,Number> performanceGraph;
    @FXML private Button logoutBtn;
    @FXML private Button userManagementBtn;
    @FXML private Button viewStockBtn;
    @FXML private Button viewReportsBtn;
    @FXML private Label errMsg;
    public void errorMessage(String msg){
        errMsg.setText(msg);
    }

    @Override
    public void init() {
        String[] xLookup=new String[31];
        for(int i=0;i<31;i++){
            xLookup[i]=String.valueOf(i+1);
        }
        try{
            int[] yLookup = PerformanceMeasurement.getMonthlyData(new Date(System.currentTimeMillis()));
            XYChart.Series<String,Number> series = new XYChart.Series<>();
            series.setName("Performance Points (PP)");
            for(int i=0;i<31;i++){
                series.getData().add(new XYChart.Data<>(xLookup[i],yLookup[i]));
            }
            performanceGraph.getData().add(series);
        }catch(Exception e){
            errorMessage(e.getMessage());
        }
    }

    public void viewReports(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("report/view-reports.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initOwner(viewReportsBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void viewStock(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("stock/view-stock.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initOwner(viewStockBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void userManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin/user-management.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initOwner(userManagementBtn.getScene().getWindow());
        UserManagementController controller = fxmlLoader.getController();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        controller.init();
        stage.setResizable(false);
        stage.show();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        Stage parent=(Stage)logoutBtn.getScene().getWindow();
        parent.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        stage.setResizable(false);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
