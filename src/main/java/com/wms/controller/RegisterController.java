package com.wms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RegisterController {
    @FXML
    private Button closeBtn;

    public void onCloseBtnClicked(ActionEvent e){
        Stage s =(Stage) closeBtn.getScene().getWindow();
        s.close();
    }

}
