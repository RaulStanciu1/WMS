package com.wms.controller;

import com.wms.data.RequestSearch;
import com.wms.data.User;
import com.wms.data.models.RequestHistoryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class WORequestHistoryController implements Controller{
    @FXML private TableView<RequestHistoryModel> requestHistoryTable;
    @FXML private TableColumn<RequestHistoryModel,String> productIdColumn;
    @FXML private TableColumn<RequestHistoryModel,String> quantityColumn;
    @FXML private TableColumn<RequestHistoryModel,String> timestampColumn;
    @FXML private TableColumn<RequestHistoryModel,String> statusColumn;

    private void fillRequestHistoryTable(int receiver) throws Exception{
        List<RequestSearch> tmpList = RequestSearch.getRequestsByReceiver(receiver);
        List<RequestHistoryModel> models = RequestHistoryModel.getRequestHistoryModels(tmpList);
        ObservableList<RequestHistoryModel> requests = FXCollections.observableList(models);
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("ProductId"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("Timestamp"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));
        requestHistoryTable.setItems(requests);
    }
    private void errorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR,msg, ButtonType.OK);
        alert.showAndWait();
    }
    @Override
    public void init() {
        User userData = (User) requestHistoryTable.getScene().getWindow().getUserData();
        try{
            requestHistoryTable.setStyle("-fx-font: 12px \"Century Gothic\";");
            fillRequestHistoryTable(userData.getId());
        }catch(Exception e){
            errorMessage(e.getMessage());
        }

    }
}
