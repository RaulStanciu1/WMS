package com.wms.controller;

import com.wms.Main;
import com.wms.data.DBConnection;
import com.wms.data.RequestSearch;
import com.wms.data.User;
import com.wms.data.models.WoRequestModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class WarehouseOperatorController implements Controller{
    @FXML private TableColumn<WoRequestModel,String> timestampColumn;
    @FXML private TableColumn<WoRequestModel,Integer> sentByColumn;
    @FXML private TableColumn<WoRequestModel,Integer> quantityColumn;
    @FXML private TableColumn<WoRequestModel,Integer> productIdColumn;
    @FXML private TableView<WoRequestModel> pendingRequestsTable;
    @FXML private TableColumn<WoRequestModel,Integer> requestIdColumn;
    @FXML private Label userId;
    @FXML private Label username;
    @FXML private Button requestHistoryBtn;
    @FXML private Button denySelectedRequestBtn;
    @FXML private Button confirmSelectedRequestBtn;
    @FXML private Button reportMisuseBtn;
    @FXML private Button logoutBtn;
    @FXML private Button viewStockBtn;
    private List<RequestSearch> modelList;
    private User userData;

    @Override
    public void init() {

        userData = (User)username.getScene().getWindow().getUserData();
        try {
            this.modelList=RequestSearch.getPendingRequestsByReceiver(userData.getId());
            ObservableList<WoRequestModel> requests = FXCollections.observableList(WoRequestModel.modelList(this.modelList));
            timestampColumn.setCellValueFactory(
                    new PropertyValueFactory<>("Timestamp")
            );
            sentByColumn.setCellValueFactory(
                    new PropertyValueFactory<>("SentBy")
            );
            quantityColumn.setCellValueFactory(
                    new PropertyValueFactory<>("Quantity")
            );
            productIdColumn.setCellValueFactory(
                    new PropertyValueFactory<>("ProductId")
            );
            requestIdColumn.setCellValueFactory(
                    new PropertyValueFactory<>("Id")
            );
            pendingRequestsTable.setItems(requests);
        }catch(Exception e){
            e.printStackTrace();
        }

        username.setText(username.getText()+" "+userData.getName());
        userId.setText(userId.getText()+" "+userData.getId());
        pendingRequestsTable.setStyle("-fx-font: 12px \"Century Gothic\";");
    }

    public void viewStock(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("stock/view-stock.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        ViewStockController controller= fxmlLoader.getController();
        stage.initOwner(viewStockBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setUserData(username.getScene().getWindow().getUserData());
        controller.init();
        stage.setResizable(false);
        stage.show();
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


    public void confirmSelectedRequest(ActionEvent actionEvent) {
        int index=pendingRequestsTable.getSelectionModel().getSelectedIndex();
        if(index==-1){
            Alert noRequestSelected = new Alert(Alert.AlertType.ERROR,"No Request Selected",ButtonType.OK);
            noRequestSelected.showAndWait();
            return;
        }
        RequestSearch rs = this.modelList.get(index);
        String SQL = "UPDATE wms.requests SET status='APPROVED' WHERE id=?";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,rs.getId());
            ps.execute();
            this.pendingRequestsTable.getItems().remove(index);
            User.getUserById(rs.getSentBy());
            this.modelList.remove(index);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }

    }

    public void denySelectedRequest(ActionEvent actionEvent) {
        int index=pendingRequestsTable.getSelectionModel().getSelectedIndex();
        if(index==-1){
            Alert noRequestSelected = new Alert(Alert.AlertType.ERROR,"No Request Selected",ButtonType.OK);
            noRequestSelected.showAndWait();
            return;
        }
        RequestSearch rs = this.modelList.get(index);
        String SQL = "UPDATE wms.requests SET status='DENIED' WHERE id=?";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,rs.getId());
            ps.execute();
            this.pendingRequestsTable.getItems().remove(index);
            this.modelList.remove(index);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void requestHistory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("wo/request-history.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        WORequestHistoryController controller = fxmlLoader.getController();
        stage.initOwner(requestHistoryBtn.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setUserData(confirmSelectedRequestBtn.getScene().getWindow().getUserData());
        controller.init();
        stage.setResizable(false);
        stage.show();
    }
}
