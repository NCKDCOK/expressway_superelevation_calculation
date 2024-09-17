package com.schdri.view;

import com.schdri.config.RoadConfig;
import com.schdri.domain.FormData;
import com.schdri.domain.OutPutData;
import com.schdri.domain.ReadData;
import com.schdri.domain.WriteData;
import com.schdri.utils.*;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.util.List;

public class RuleDefinitionView extends BaseView {
    private FormData formData;
    private RoadConfig roadConfig;

    private ComboBox<String> roadClassCombo;
    private ComboBox<Integer> speedCombo;
    private ComboBox<String> regionCombo;
    private ComboBox<String> rotationAxisCombo;
    private TextField laneWidthField;
    private TextField laneSlopeField;
    private TextField hardShoulderWidthField;
    private TextField hardShoulderSlopeField;
    private TextField softShoulderWidthField;
    private TextField softShoulderSlopeField;
    private CheckBox hardShoulderSuperelevationCheck;
    private CheckBox softShoulderSuperelevationCheck;
    private Label superelevationLabel;
    private Label noteLabel;

    public RuleDefinitionView() {
        super();
        this.formData = FormDataManager.getInstance().getFormData();
        this.roadConfig = RoadConfig.load();
        this.getStylesheets().add(getClass().getResource("/com/schdri/css/ruleDefinition.css").toExternalForm());
        this.getStyleClass().add("rule-definition-view");

        initializeUI();
        bindData();
    }

    private void initializeUI() {
        VBox mainContainer = new VBox(20);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = createTitleLabel("规则定义");
        titleLabel.getStyleClass().add("content-title");

        Node content = createContent();

        mainContainer.getChildren().addAll(titleLabel, content);
        this.getChildren().add(mainContainer);
    }

    private Node createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("content-box");


        roadClassCombo = new ComboBox<>();
        roadClassCombo.getItems().addAll(roadConfig.getRoadClassNames());
        roadClassCombo.setOnAction(e -> updateSpeedOptions());

        speedCombo = new ComboBox<>();
        regionCombo = new ComboBox<>();
        regionCombo.getItems().addAll("一般地区", "积雪冰冻地区", "城镇区域");
        regionCombo.setOnAction(e -> updateSuperelevationLimit());

        rotationAxisCombo = createComboBox("中线","边线");
        laneWidthField = new TextField();
        laneSlopeField = new TextField();
        hardShoulderWidthField = new TextField();
        hardShoulderSlopeField = new TextField();
        softShoulderWidthField = new TextField();
        softShoulderSlopeField = new TextField();
        hardShoulderSuperelevationCheck = new CheckBox("硬路肩超高");
        softShoulderSuperelevationCheck = new CheckBox("土路肩超高");


        superelevationLabel = new Label();
        noteLabel = new Label();

        content.getChildren().addAll(
                createOptionRow("道路等级:", roadClassCombo),
                createOptionRow("设计速度 (km/h):", speedCombo),
                createOptionRow("地区:", regionCombo),
                createOptionRow("旋转轴:", rotationAxisCombo),
                createOptionRow("半幅行车道宽度(m):", laneWidthField),
                createOptionRow("半幅行车道坡度(%):", laneSlopeField),
                createOptionRow("半幅硬路肩宽度(m):", hardShoulderWidthField),
                createOptionRow("半幅硬路肩坡度(%):", hardShoulderSlopeField),
                createOptionRow("半幅土路肩宽度(m):", softShoulderWidthField),
                createOptionRow("半幅土路肩坡度(%):", softShoulderSlopeField),
                createCheckBoxRow(hardShoulderSuperelevationCheck, softShoulderSuperelevationCheck),
                createLabelRow("最大超高值:", superelevationLabel),
                createSaveButton()
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scroll-pane");

        return scrollPane;
    }

    private HBox createCheckBoxRow(CheckBox... checkBoxes) {
        HBox row = new HBox(20);
        row.getStyleClass().add("option-row");
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setStyle("-fx-font-size: 18px;");
            row.getChildren().add(checkBox);
        }
        return row;
    }

    private HBox createOptionRow(String label, Control field) {
        HBox row = new HBox(10);
        row.getStyleClass().add("option-row");
        Label labelNode = new Label(label);
        labelNode.setMinWidth(200);
        row.getChildren().addAll(labelNode, field);
        return row;
    }

    private HBox createLabelRow(String label, Label valueLabel) {
        HBox row = new HBox(10);
        row.getStyleClass().add("option-row");
        Label labelNode = new Label(label);
        labelNode.setMinWidth(200);
        row.getChildren().addAll(labelNode, valueLabel);
        return row;
    }

    private <T> ComboBox<T> createComboBox(T... items) {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(items);
        return comboBox;
    }

    private Node createSaveButton() {
        Button saveButton = new Button("输出结果");
        saveButton.getStyleClass().add("nav-button");
        saveButton.setOnAction(e -> saveRules());

        HBox buttonContainer = new HBox(saveButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(10, 0, 0, 0)); // 添加一些顶部内边距

        return buttonContainer;
    }

    private void bindData() {
        roadClassCombo.valueProperty().bindBidirectional(formData.roadClassProperty());
        speedCombo.valueProperty().bindBidirectional(formData.speedProperty());
        regionCombo.valueProperty().bindBidirectional(formData.regionProperty());
        rotationAxisCombo.valueProperty().bindBidirectional(formData.rotationAxisProperty());

        bindTextFieldToDoubleProperty(laneWidthField, formData.laneWidthProperty());
        bindTextFieldToDoubleProperty(laneSlopeField, formData.laneSlopeProperty());
        bindTextFieldToDoubleProperty(hardShoulderWidthField, formData.hardShoulderWidthProperty());
        bindTextFieldToDoubleProperty(hardShoulderSlopeField, formData.hardShoulderSlopeProperty());
        bindTextFieldToDoubleProperty(softShoulderWidthField, formData.softShoulderWidthProperty());
        bindTextFieldToDoubleProperty(softShoulderSlopeField, formData.softShoulderSlopeProperty());

        hardShoulderSuperelevationCheck.selectedProperty().bindBidirectional(formData.hardShoulderSuperelevationProperty());
        softShoulderSuperelevationCheck.selectedProperty().bindBidirectional(formData.softShoulderSuperelevationProperty());
        //superelevationLabel.textProperty().bind(formData.maxSuperelevationProperty().asString());
    }

    private void bindTextFieldToDoubleProperty(TextField textField, DoubleProperty property) {
        textField.textProperty().bindBidirectional(property, new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                return number == null ? "" : number.toString();
            }

            @Override
            public Number fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        });
    }

    private void updateSpeedOptions() {
        String selectedRoadClass = roadClassCombo.getValue();
        if (selectedRoadClass != null) {
            List<Integer> speeds = roadConfig.getSpeedsForRoadClass(selectedRoadClass);
            speedCombo.getItems().clear();
            speedCombo.getItems().addAll(speeds);
        }
    }

    private void updateSuperelevationLimit() {
        String roadType = roadClassCombo.getValue();
        String region = regionCombo.getValue();
        if (roadType != null && region != null) {
            try {
                int limit = roadConfig.getSuperelevationLimit(roadType, region);
                superelevationLabel.setText(limit + "%");

                String note = roadConfig.getSuperelevationNote(roadType, region);
                noteLabel.setText(note != null ? note : "");

                // 将 limit 赋值给 maxSuperelevation 属性
                formData.maxSuperelevationProperty().set(limit);
            } catch (IllegalArgumentException e) {
                superelevationLabel.setText("未找到匹配的超高限制");
                noteLabel.setText("");
            }
        }
    }

    private void saveRules() {
        if (validateInputs()) {
            System.out.println("开始计算");
            String filePath = formData.filePathProperty().getValue(); // 输入文件路径

            String outputFilePath = selectFile();
            if (outputFilePath != null) {
                processExcelFile(filePath, outputFilePath);
                showMessage("计算完成，结果已保存到 " + outputFilePath, true);
                formData.clearAll();
            } else {
                showMessage("未选择保存位置，操作取消", false);
            }
        } else {
            showMessage("请填写所有必填字段，并确保数值输入正确。",false);
        }
    }

    // 文件保存选择
    private String selectFile() {
        // 创建文件选择器
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存文件");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel 文件", "*.xlsx")
        );

        // 设置默认文件名
        fileChooser.setInitialFileName("output.xlsx");

        // 显示保存文件对话框
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            String filePath = file.getAbsolutePath();

            // 确保文件名以.xlsx结尾
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            return filePath;
        }

        return null; // 如果用户取消了操作，返回null
    }



    private boolean validateInputs() {
        boolean isValid = roadClassCombo != null && roadClassCombo.getValue() != null &&
                speedCombo != null && speedCombo.getValue() != null &&
                regionCombo != null && regionCombo.getValue() != null &&
                rotationAxisCombo != null && rotationAxisCombo.getValue() != null &&
                laneWidthField != null && laneWidthField.getText() != null && !laneWidthField.getText().trim().isEmpty() &&
                laneSlopeField != null && laneSlopeField.getText() != null && !laneSlopeField.getText().trim().isEmpty() &&
                hardShoulderWidthField != null && hardShoulderWidthField.getText() != null && !hardShoulderWidthField.getText().trim().isEmpty() &&
                hardShoulderSlopeField != null && hardShoulderSlopeField.getText() != null && !hardShoulderSlopeField.getText().trim().isEmpty() &&
                softShoulderWidthField != null && softShoulderWidthField.getText() != null && !softShoulderWidthField.getText().trim().isEmpty() &&
                softShoulderSlopeField != null && softShoulderSlopeField.getText() != null && !softShoulderSlopeField.getText().trim().isEmpty();

        return isValid;
    }

    private  void showMessage(String message, boolean isSuccess) {
        Alert alert;
        if (isSuccess) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("操作成功");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("操作失败");
        }

        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/com/schdri/css/alter.css").toExternalForm());

        dialogPane.getStyleClass().add(isSuccess ? "custom-success-alert" : "custom-error-alert");

        alert.showAndWait();
    }

    private  void processExcelFile(String filePath, String outputFilePath) {
        System.out.println("文件处理中: " + filePath);
        try {
            List<ReadData> dataList = ExcelUtil.readExcel(filePath);
            //处理readData
            List<ReadData> data = DataUtil.getReadDataList(dataList);
            System.out.println("共读取到 " + data.size() + " 条数据");

            //处理writeData
            List<WriteData> writeData = DataUtil.getWriteDataList(data);
            CurveTypeUtil.determineCurveTypes(writeData);
            CurveSuperelevationUtil.getSuperElevation(writeData);
            CalculateUtil.calculateDeflectionAngles(writeData);
            TransitionCalculatorUtil.calculateTransitionPoints(writeData);
            List<OutPutData> outPutDataList = FinallyUtil.finallyCalculate(writeData);
            ExcelUtil.output(outputFilePath, outPutDataList);

            //ExcelUtil.writeExcel(outputFilePath, writeData);




        } catch (Exception e) {
            System.out.println("处理文件时出错: " + e.getMessage());
            showMessage("处理文件时出错: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

}