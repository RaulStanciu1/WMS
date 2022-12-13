package com.wms.controller;

import com.wms.Main;
import com.wms.data.DBConnection;
import com.wms.data.Product;
import com.wms.data.RequestInsert;
import com.wms.data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProductionOperatorController implements Controller{
    @FXML private Button requestHistoryBtn;
    @FXML private Button reportMisuseBtn;
    @FXML private Button sendRequestBtn;
    @FXML private TextField quantity;
    @FXML private ComboBox<Integer> products;
    @FXML private Button logoutBtn;
    @FXML private Label username;
    @FXML private Label errMsg;
    @FXML private Label successMsg;
    public void errorMessage(String msg){
        errMsg.setText(msg);
    }
    public void successMessage(String msg){
        successMsg.setText(msg);
    }
    public void init(){
        User userInfo = (User) username.getScene().getWindow().getUserData();
        username.setText(userInfo.getName());
        try {
            products.getItems().addAll(Product.getProductIds(Product.getProductsFromDB()));
        }catch(Exception e){
            errorMessage(e.getMessage());
        }
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
    public void insertRequestToDB(RequestInsert ri) throws SQLException{
        String SQL = "Insert Into wms.requests (sender,receiver,product_id,quantity,timestamp,status) VALUES (?,?,?,?,?,'PENDING')";

        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,ri.getSender());
            ps.setInt(2,ri.getReceiver());
            ps.setInt(3,ri.getProductId());
            ps.setInt(4,ri.getQuantity());
            ps.setDate(5,ri.getTimestamp());
            ps.execute();
            ps.close();
        }catch(SQLException e){
            throw new SQLException("Something Went Wrong");
        }

    }

    public void sendRequest(ActionEvent actionEvent) {
        int amount;
        User userData = (User) username.getScene().getWindow().getUserData();
        try{
            errorMessage("");
            successMessage("");
            amount=Integer.parseInt(quantity.getText());
            int productId=products.getSelectionModel().getSelectedItem();
            insertRequestToDB(new RequestInsert(userData.getId(),productId,amount,new Date(System.currentTimeMillis())));
            successMessage("Request Sent Successfully");
        }catch(SQLException e){
            errorMessage("Something Went Wrong");
        }
        catch(Exception e){
            errorMessage("Invalid Quantity");
        }
    }


    public void openReportWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("report/make-report.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initOwner(reportMisuseBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void openRequestHistory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("po/request-history.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initOwner(requestHistoryBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
