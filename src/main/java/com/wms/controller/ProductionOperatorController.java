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
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductionOperatorController implements Controller{
    @FXML private Button requestHistoryBtn;
    @FXML private TextField quantity;
    @FXML private Button logoutBtn;
    @FXML private Label username;
    private User userData;
    @FXML private TabPane productsContainer;
    public void errorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR,msg,ButtonType.OK);
        alert.showAndWait();
    }
    public void successMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,msg,ButtonType.OK);
        alert.showAndWait();
    }
    public void init(){
        userData = (User) username.getScene().getWindow().getUserData();
        username.setText(userData.getName());
        productsContainer.setStyle("\"-fx-font: 20px \\\"Century Gothic\\\";\"");
        try {
            List<Product> productList = Product.getProductsFromDB();
            for(Product p:productList){
                Tab productTab = new Tab(String.valueOf(p.id()));
                productTab.setUserData(p);
                productTab.setContent(new ProductContainer(p).getPane());
                productsContainer.getTabs().add(productTab);
            }
        }catch(Exception e){
            errorMessage(e.getMessage());
        }
    }

    public void logout(ActionEvent actionEvent) throws IOException {
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

    public void sendRequest() {
        int amount;
        userData = (User) username.getScene().getWindow().getUserData();
        try{
            amount=Integer.parseInt(quantity.getText());
            int productId=((Product)productsContainer.getSelectionModel().getSelectedItem().getUserData()).id();
            insertRequestToDB(new RequestInsert(userData.getId(),productId,amount,new Date(System.currentTimeMillis())));
            quantity.setText("");
            successMessage("Request Sent Successfully");
        }catch(SQLException e){
            errorMessage("Something Went Wrong");
        }
        catch(Exception e){
            errorMessage("Invalid Quantity");
        }
    }

    public void openRequestHistory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("po/request-history.fxml"));
        Parent root = fxmlLoader.load();
        PORequestHistoryController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.initOwner(requestHistoryBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setUserData(userData);
        controller.init();
        stage.show();
    }
}
