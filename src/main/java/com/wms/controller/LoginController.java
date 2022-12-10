package com.wms.controller;

import com.wms.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private Button loginBtn;
    @FXML
    private Label errMsg;
    @FXML
    private Hyperlink toRegisterPage;
    public void errorMessage(String msg){
        errMsg.setText(msg);
    }
    public void onCloseBtnClicked(ActionEvent e){
        Stage s =(Stage)closeBtn.getScene().getWindow();
        s.close();
    }
    public void openRegisterPage(ActionEvent e) throws IOException {
        Stage s=(Stage)toRegisterPage.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("register.fxml"));
        Scene registerScene = new Scene(loader.load(),520,400);
        s.setScene(registerScene);
    }

    public void login(ActionEvent e) throws IOException{
        Stage oldStage = (Stage) loginBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("admin/admin.fxml"));
        Parent root = loader.load();
        Scene mainScene= new Scene(root);
        Stage newWindow = new Stage();
        newWindow.setScene(mainScene);
        newWindow.setResizable(false);
        newWindow.setTitle("WMS - Production Operator Page");
        newWindow.show();
        oldStage.close();
    }

}