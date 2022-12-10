package com.wms.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        stage.setResizable(false);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}