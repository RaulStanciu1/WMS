package com.wms.controller;

import com.wms.Main;
import com.wms.data.analysis.PerformanceMeasurement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Date;

public class AdminController implements Controller{
    @FXML private LineChart<String,Number> performanceGraph;
    @FXML private Button logoutBtn;
    @FXML private Button userManagementBtn;
    @FXML private Button viewStockBtn;
    public void errorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR,msg, ButtonType.OK);
        alert.showAndWait();
    }

    @Override
    public void init() {
        String[] xLookup=new String[31];
        for(int i=0;i<31;i++){
            xLookup[i]=String.valueOf(i+1);
        }
        try{
            performanceGraph.setStyle("-fx-font: 12px \"Century Gothic\";");
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

    public void viewAMS() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("report/view-reports.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        ViewReportsController controller = fxmlLoader.getController();
        stage.initOwner(logoutBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        controller.init();
        stage.setResizable(false);
        stage.show();
    }
    public void viewDailyAnalysis() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin/daily-analysis.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        DailyAnalysisController controller = fxmlLoader.getController();
        stage.initOwner(logoutBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        controller.init();
        stage.setResizable(false);
        stage.show();
    }

    public void viewStock() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("stock/view-stock.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        ViewStockController controller = fxmlLoader.getController();
        stage.initOwner(viewStockBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        controller.init();
        stage.setResizable(false);
        stage.show();
    }

    public void userManagement() throws IOException {
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

    public void newProduct() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin/new-product.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initOwner(userManagementBtn.getScene().getWindow());
        NewProductController controller = fxmlLoader.getController();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        controller.init();
        stage.setResizable(false);
        stage.show();
    }

    public void logout() throws IOException {
        Stage parent=(Stage)logoutBtn.getScene().getWindow();
        parent.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
