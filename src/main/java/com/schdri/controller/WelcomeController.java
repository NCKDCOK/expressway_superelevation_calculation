package com.schdri.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private Button startButton;

    private Stage stage;

    @FXML
    private void initialize() {
        startButton.setOnAction(event -> loadMainScene());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadMainScene() {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("/com/schdri/fxml/main.fxml"));
            Scene mainScene = new Scene(mainRoot, 920, 720);
            // 如果主界面有独立的CSS，可以在这里加载
            stage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
            // 这里可以添加错误处理，比如显示一个错误对话框
        }
    }
}