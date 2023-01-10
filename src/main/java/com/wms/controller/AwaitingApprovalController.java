package com.wms.controller;

import com.wms.Main;
import com.wms.data.DBConnection;
import com.wms.data.User;
import com.wms.data.models.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class AwaitingApprovalController implements Controller{
    @FXML private Button approveSelectedUserBtn;
    @FXML private Button denySelectedUserBtn;
    @FXML private Button viewCurrentUsersBtn;
    @FXML private Button closeBtn;
    @FXML private TableView<UserModel> usersAwaitingApprovalTable;
    @FXML private TableColumn<UserModel,Integer> idColumn;
    @FXML private TableColumn<UserModel,String> usernameColumn;
    @FXML private TableColumn<UserModel,String> nameColumn;
    @FXML private TableColumn<UserModel,String> positionColumn;
    private List<User> users;

    @Override
    public void init() {
        users=User.getAllPendingUsers();
        ObservableList<UserModel> userModels = FXCollections.observableList(UserModel.listModel(users));
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("Id")
        );
        usernameColumn.setCellValueFactory(
                new PropertyValueFactory<>("Username")
        );
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("Name")
        );
        positionColumn.setCellValueFactory(
                new PropertyValueFactory<>("Position")
        );
        usersAwaitingApprovalTable.setItems(userModels);
    }

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        Stage s = (Stage)closeBtn.getScene().getWindow();
        s.close();
    }

    public void viewCurrentUsers(ActionEvent actionEvent) throws IOException {
        Stage s=(Stage)viewCurrentUsersBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("admin/user-management.fxml"));
        Scene registerScene = new Scene(loader.load());
        UserManagementController controller= loader.getController();
        controller.init();
        s.setScene(registerScene);
    }

    public void approveSelectedUser(){
        int index=usersAwaitingApprovalTable.getSelectionModel().getSelectedIndex();
        User u = this.users.get(index);
        String SQL = "UPDATE wms.users SET status='APPROVED' WHERE id=?";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,u.getId());
            ps.execute();
            this.usersAwaitingApprovalTable.getItems().remove(index);
            this.users.remove(index);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    public void denySelectedUser(){
        int index=usersAwaitingApprovalTable.getSelectionModel().getSelectedIndex();
        User u = this.users.get(index);
        String SQL = "UPDATE wms.users SET status='DENIED' WHERE id=?";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,u.getId());
            ps.execute();
            this.usersAwaitingApprovalTable.getItems().remove(index);
            this.users.remove(index);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
