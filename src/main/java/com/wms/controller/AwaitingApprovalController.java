package com.wms.controller;

import com.wms.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AwaitingApprovalController {
    @FXML private Button viewCurrentUsersBtn;
    @FXML private Button closeBtn;

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        Stage s = (Stage)closeBtn.getScene().getWindow();
        s.close();
    }

    public void viewCurrentUsers(ActionEvent actionEvent) throws IOException {
        Stage s=(Stage)viewCurrentUsersBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("admin/user-management.fxml"));
        Scene registerScene = new Scene(loader.load());
        s.setScene(registerScene);
    }
}
