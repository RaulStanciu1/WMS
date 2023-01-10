package com.wms.controller;

import com.wms.Main;
import com.wms.data.Position;
import com.wms.data.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController implements Controller{
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;
    @FXML private TextField displayName;
    @FXML private ComboBox<Position> positions;
    @FXML private TextField username;
    @FXML private Hyperlink toLoginPageBtn;
    @FXML private Button closeBtn;
    @FXML private Label errMsg;
    @FXML private Label successMsg;
    public void init(){
        positions.getItems().addAll(Position.WAREHOUSE_OP,Position.PRODUCTION_OP);
        positions.getSelectionModel().selectFirst();
        positions.setStyle("-fx-font: 18px \"Century Gothic\";");
    }
    public void errorMessage(String msg){
        errMsg.setText(msg);
    }
    public void successMessage(String msg){
        successMsg.setText(msg);
    }

    public void onCloseBtnClicked(ActionEvent ae){
        Stage s =(Stage) closeBtn.getScene().getWindow();
        s.close();
    }

    public void toLoginPage(ActionEvent ae) throws IOException {
        Stage s=(Stage)toLoginPageBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene loginScene = new Scene(loader.load());
        s.setScene(loginScene);
    }

    public void register(ActionEvent ae){
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        String confirmPasswordInput = confirmPassword.getText();
        String displayNameInput = displayName.getText();
        Position positionInput = positions.getSelectionModel().getSelectedItem();
        try{
            errorMessage("");
            Validation.registerValid(usernameInput,passwordInput,confirmPasswordInput,displayNameInput,positionInput);
            successMessage("Account Sent For Admin Approval");
            username.setText("");
            password.setText("");
            confirmPassword.setText("");
            displayName.setText("");
            positions.getSelectionModel().selectFirst();
        }catch(Exception e){
            errorMessage(e.getMessage());
        }
    }
}
