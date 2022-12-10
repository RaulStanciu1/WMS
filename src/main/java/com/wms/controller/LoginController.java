package com.wms.controller;

import com.wms.ui.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button closeBtn;
    @FXML
    private Label errMsg;
    @FXML
    private Hyperlink toRegisterPage;
    public void onCloseBtnClicked(ActionEvent e){
        Stage s =(Stage)closeBtn.getScene().getWindow();
        s.close();
    }
    public void openRegisterPage(ActionEvent e) throws IOException {
        Stage s=(Stage)toRegisterPage.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Login.class.getResource("register.fxml"));
        Scene registerScene = new Scene(loader.load(),520,400);
        s.setScene(registerScene);
    }


}