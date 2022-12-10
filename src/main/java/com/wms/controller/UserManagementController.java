package com.wms.controller;

import com.wms.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserManagementController {
    @FXML private Button viewUsersAwaitingApprovalBtn;
    @FXML private Button removeSelectedUserBtn;
    @FXML private Button closeBtn;

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        Stage s = (Stage) closeBtn.getScene().getWindow();
        s.close();
    }

    public void removeSelectedUser(ActionEvent actionEvent) {
        //TODO: Implementation
    }

    public void viewUsersAwaitingApproval(ActionEvent actionEvent) throws IOException {
        Stage s=(Stage)viewUsersAwaitingApprovalBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("admin/awaiting-approval.fxml"));
        Scene registerScene = new Scene(loader.load());
        s.setScene(registerScene);
    }
}
