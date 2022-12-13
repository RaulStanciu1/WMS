package com.wms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WORequestHistoryController implements Controller{
    @FXML private Button closeBtn;
    public void init(){

    }

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        Stage s = (Stage)  closeBtn.getScene().getWindow();
        s.close();
    }
}
