package com.wms.controller;

import com.wms.Main;
import com.wms.data.Position;
import com.wms.data.User;
import com.wms.data.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button closeBtn;
    @FXML private Button loginBtn;
    @FXML private Label errMsg;
    @FXML private Hyperlink toRegisterPage;
    public void errorMessage(String msg){
        errMsg.setText(msg);
    }
    public void onCloseBtnClicked(ActionEvent ae){
        Stage s =(Stage)closeBtn.getScene().getWindow();
        s.close();
    }
    public void openRegisterPage(ActionEvent ae) throws IOException {
        Stage s=(Stage)toRegisterPage.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("register.fxml"));
        Scene registerScene = new Scene(loader.load(),520,400);
        RegisterController rc = loader.getController();
        rc.init();
        s.setScene(registerScene);
    }
    private void loginHelper(String resourcePath,String title) throws IOException{
        Stage oldStage = (Stage) loginBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(resourcePath));
        Parent root = loader.load();
        Scene mainScene= new Scene(root);
        Stage newWindow = new Stage();
        newWindow.setScene(mainScene);
        newWindow.setResizable(false);
        newWindow.setTitle(title);
        newWindow.show();
        oldStage.close();
    }

    public void login(ActionEvent ae) throws IOException{
        String usernameInput=username.getText();
        String passwordInput=password.getText();
        try{
            errorMessage("");
            Validation.loginValid(usernameInput,passwordInput);
            User loginUser = Validation.getUserFromDB(usernameInput);
            switch (loginUser.getPosition()) {
                case ADMIN -> loginHelper("admin/admin.fxml", "WMS - Admin Window");
                case WAREHOUSE_OP -> loginHelper("wo/warehouse-operator.fxml", "WMS - Warehouse Operator");
                case PRODUCTION_OP -> loginHelper("po/production-operator.fxml", "WMS - Production Operator");
                default -> errorMessage("Something Went Wrong");
            }
        }catch(Exception e){
            username.setText("");
            password.setText("");
            errorMessage(e.getMessage());
        }
    }

}