package com.schdri;

import com.schdri.controller.WelcomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {


    static {
        try {
            String javaFxPath = System.getenv("JAVAFX_HOME") + "/lib";
            String modulePath = System.getProperty("jdk.module.path");
            if (modulePath == null || modulePath.isEmpty()) {
                System.setProperty("jdk.module.path", javaFxPath);
            } else {
                System.setProperty("jdk.module.path", modulePath + File.pathSeparator + javaFxPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {



        // 加载欢迎界面
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/schdri/fxml/welcome.fxml"));
        Parent welcomeRoot = loader.load();
        Scene welcomeScene = new Scene(welcomeRoot, 920, 720);
        welcomeScene.getStylesheets().add(getClass().getResource("/com/schdri/css/welcome.css").toExternalForm());

        // 设置窗口标题
        primaryStage.setTitle("四川交院超高计算程序");

        // 设置应用图标
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/schdri/img/icon.ico")));

        primaryStage.setScene(welcomeScene);
        primaryStage.setResizable(false); // 设置窗口不可调整大小

        // 获取控制器并设置 Stage
        WelcomeController controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {


        launch(args);
    }
}