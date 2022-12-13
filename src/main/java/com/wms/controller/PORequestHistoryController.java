package com.wms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PORequestHistoryController implements Controller{
    @FXML private Button closeBtn;

    @Override
    public void init() {

    }

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        Stage s = (Stage) closeBtn.getScene().getWindow();
        s.close();
    }
}
