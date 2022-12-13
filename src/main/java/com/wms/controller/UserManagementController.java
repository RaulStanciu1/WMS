package com.wms.controller;

import com.wms.Main;
import com.wms.data.DBConnection;
import com.wms.data.User;
import com.wms.data.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class UserManagementController implements Controller{
    @FXML private Button viewUsersAwaitingApprovalBtn;
    @FXML private Button removeSelectedUserBtn;
    @FXML private Button closeBtn;
    @FXML private TableView<UserModel> currentUsersTable;
    @FXML private TableColumn<UserModel,Integer> idColumn;
    @FXML private TableColumn<UserModel,String> usernameColumn;
    @FXML private TableColumn<UserModel,String> nameColumn;
    @FXML private TableColumn<UserModel,String> positionColumn;
    private List<User> users;

    @Override
    public void init() {
        users=User.getAllUsers();
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
        currentUsersTable.setItems(userModels);
    }

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        Stage s = (Stage) closeBtn.getScene().getWindow();
        s.close();
    }

    public void removeSelectedUser(ActionEvent actionEvent) {
        int index=currentUsersTable.getSelectionModel().getSelectedIndex();
        User u = this.users.get(index);
        String SQL = "DELETE FROM wms.users WHERE id=?";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,u.getId());
            ps.execute();
            this.currentUsersTable.getItems().remove(index);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void viewUsersAwaitingApproval(ActionEvent actionEvent) throws IOException {
        Stage s=(Stage)viewUsersAwaitingApprovalBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("admin/awaiting-approval.fxml"));
        Scene registerScene = new Scene(loader.load());
        AwaitingApprovalController controller = loader.getController();
        controller.init();
        s.setScene(registerScene);
    }
}
