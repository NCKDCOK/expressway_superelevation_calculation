package com.schdri.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schdri.domain.FormData;
import com.schdri.domain.WriteData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CurveSuperelevationUtil {
    private static JsonNode root;
    /*private static final int designSpeed = 30;
    private static final String condition = "normal";
    private static final int maxSuperelevation = 8;*/

    private static void loadJsonIfNeeded() throws IOException {
        if (root == null) {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = CurveSuperelevationUtil.class.getResourceAsStream("/com/schdri/json/data.json");
            root = mapper.readTree(inputStream);
        }
    }

    public static int querySuperelevation(double radius) throws IOException {
        FormData formData = FormDataManager.getInstance().getFormData();
        int designSpeed = formData.speedProperty().get();
        String condition = formData.roadTypeProperty().toString().equals("积雪冰冻地区")?"icy" : "normal";
        int maxSuperelevation = formData.maxSuperelevationProperty().get();


        JsonNode speedNode = root.path("curveSuperelevationTable").path(String.valueOf(designSpeed));
        JsonNode conditionNode = speedNode.path(condition);

        JsonNode rangeNode;
        if ("normal".equals(condition)) {
            rangeNode = conditionNode.path(String.valueOf(maxSuperelevation));
        } else {
            rangeNode = conditionNode;
        }

        for (JsonNode entry : rangeNode) {
            double minRadius = entry.path("minRadius").asDouble();
            JsonNode maxRadiusNode = entry.path("maxRadius");
            double maxRadius = maxRadiusNode.isNull() ? Double.MAX_VALUE : maxRadiusNode.asDouble();

            if (radius >= minRadius && radius <= maxRadius) {
                return entry.path("superelevation").asInt();
            }
        }

        return -1; // 没有找到匹配的超高值
    }

    public static void getSuperElevation(List<WriteData> dataList) throws IOException {
        loadJsonIfNeeded();

        // 用来记录半径过小的数据
        List<WriteData> problematicDataList = new ArrayList<>();

        for (WriteData data : dataList) {
            String radiusString = data.getRadius();
            if (radiusString == null || radiusString.trim().isEmpty()) {
                data.setSuperelevation("");
                continue;
            }

            try {
                double radius = Double.parseDouble(radiusString);
                if (radius >= 350.00) {
                    data.setSuperelevation("半径＞350，不需设置超高");
                } else {
                    int superelevation = querySuperelevation(radius);
                    if (superelevation == -1) {
                        // 记录半径过小的数据
                        problematicDataList.add(data);
                    } else {
                        data.setSuperelevation(String.valueOf(superelevation));
                    }
                }
            } catch (NumberFormatException e) {
                data.setSuperelevation("");
            }
        }

        // 如果有问题数据，弹出 JavaFX 对话框
        if (!problematicDataList.isEmpty()) {
            showSuperelevationInputDialog(problematicDataList);
        }
    }

    private static void showSuperelevationInputDialog(List<WriteData> problematicDataList) {
        // 创建一个新的 JavaFX 弹窗
        Stage dialogStage = new Stage();
        dialogStage.setTitle("手动输入超高值");

        // 创建主要的 VBox 容器
        VBox mainVBox = new VBox(10);
        mainVBox.setPadding(new Insets(10));

        // 创建用于输入字段的 VBox
        VBox inputVBox = new VBox(10);
        List<TextField> inputFields = new ArrayList<>();

        // 为每个问题数据创建一个输入框
        for (WriteData data : problematicDataList) {
            HBox hbox = new HBox(10);
            Label radiusLabel = new Label("半径：" + data.getRadius());
            TextField superelevationInput = new TextField();
            superelevationInput.setPromptText("请输入超高值");

            hbox.getChildren().addAll(radiusLabel, superelevationInput);
            inputVBox.getChildren().add(hbox);

            inputFields.add(superelevationInput);
        }

        // 创建 ScrollPane 并添加输入字段 VBox
        ScrollPane scrollPane = new ScrollPane(inputVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(400); // 设置首选视口高度

        // 创建提交和取消按钮
        Button submitButton = new Button("提交");
        Button cancelButton = new Button("取消");
        HBox buttonBox = new HBox(10, submitButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        submitButton.setOnAction(e -> {
            boolean allValid = true;
            for (int i = 0; i < problematicDataList.size(); i++) {
                WriteData data = problematicDataList.get(i);
                TextField inputField = inputFields.get(i);
                String userInput = inputField.getText();
                if (userInput == null || userInput.trim().isEmpty()) {
                    allValid = false;
                    inputField.setStyle("-fx-border-color: red;");
                } else {
                    try {
                        int userSuperelevation = Integer.parseInt(userInput);
                        data.setSuperelevation(String.valueOf(userSuperelevation));
                        inputField.setStyle("");
                    } catch (NumberFormatException ex) {
                        allValid = false;
                        inputField.setStyle("-fx-border-color: red;");
                    }
                }
            }
            if (allValid) {
                dialogStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("输入错误");
                alert.setHeaderText(null);
                alert.setContentText("请确保所有字段都填写了有效的数字。");
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        // 将 ScrollPane 和按钮添加到主 VBox
        mainVBox.getChildren().addAll(scrollPane, buttonBox);

        mainVBox.getStyleClass().add("main-vbox");
        scrollPane.getStyleClass().add("scroll-pane");
        inputVBox.getStyleClass().add("input-vbox");

        // 创建场景并显示
        Scene scene = new Scene(mainVBox);
        dialogStage.setScene(scene);
        dialogStage.setWidth(400);  // 设置窗口宽度
        dialogStage.setHeight(500); // 设置窗口高度
        scene.getStylesheets().add(CurveSuperelevationUtil.class.getResource("/com/schdri/css/dialog.css").toExternalForm());

        // 显示对话框并等待它关闭
        dialogStage.showAndWait();
    }
}