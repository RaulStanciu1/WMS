package com.wms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MakeReportController implements Controller{
    @FXML private Button sendBtn;
    @FXML private Label errMsg;
    @FXML private Button closeBtn;
    public void errorMessage(String msg){
        errMsg.setText(msg);
    }

    @Override
    public void init() {

    }

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        Stage s = (Stage) closeBtn.getScene().getWindow();
        s.close();
    }

    public void sendReport(ActionEvent actionEvent) {
        //TODO: IMPLEMENTANTION
    }
}
