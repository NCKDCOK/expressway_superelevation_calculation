package com.schdri.view;

import com.schdri.controller.MainController;
import com.schdri.domain.FormData;
import com.schdri.utils.FormDataManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;


public class DataInputView extends BaseView {

    private FormData formData;
    private ComboBox<String> dataSourceComboBox;
    private TextField filePathField;
    private Button browseButton;
    private Button previousButton;
    private Button nextButton;
    private VBox dataContainer;

    public DataInputView() {
        super();
        this.formData = FormDataManager.getInstance().getFormData();
        this.getStylesheets().add(getClass().getResource("/com/schdri/css/dataInput.css").toExternalForm());
        this.getStyleClass().add("data-input-view");

        initializeUI();
        bindFormData();
    }

    private void initializeUI() {
        VBox mainContainer = new VBox(20);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = createTitleLabel("数据输入");
        titleLabel.getStyleClass().add("content-title");

        GridPane grid = createInputGrid();
        ScrollPane dataDisplayArea = createDataDisplayArea();
        HBox buttonBox = createButtonBox();

        mainContainer.getChildren().addAll(titleLabel, grid, dataDisplayArea, buttonBox);
        this.getChildren().add(mainContainer);
    }

    private void bindFormData() {
        dataSourceComboBox.valueProperty().bindBidirectional(formData.dataSourceProperty());
        filePathField.textProperty().bindBidirectional(formData.filePathProperty());
    }

    private GridPane createInputGrid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid-pane");
        grid.setHgap(10);
        grid.setVgap(10);

        // 设置列约束
        ColumnConstraints labelColumn = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        labelColumn.setHgrow(Priority.NEVER);

        ColumnConstraints inputColumn = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        inputColumn.setHgrow(Priority.ALWAYS);

        ColumnConstraints buttonColumn = new ColumnConstraints(80, 80, Double.MAX_VALUE);
        buttonColumn.setHgrow(Priority.NEVER);

        grid.getColumnConstraints().addAll(labelColumn, inputColumn, buttonColumn);

        // 数据来源选择
        Label dataSourceLabel = new Label("数据来源:");
        grid.add(dataSourceLabel, 0, 0);
        dataSourceComboBox = new ComboBox<>();
        dataSourceComboBox.getItems().addAll("Excel", "手动输入信息", "纬地设计文件");
        dataSourceComboBox.setPromptText("选择数据来源");
        dataSourceComboBox.setMaxWidth(Double.MAX_VALUE);
        dataSourceComboBox.setOnAction(e -> handleDataSourceSelection());
        grid.add(dataSourceComboBox, 1, 0, 2, 1);

        // 文件路径输入
        Label filePathLabel = new Label("文件路径:");
        grid.add(filePathLabel, 0, 1);
        filePathField = new TextField();
        filePathField.setDisable(true);
        filePathField.setMaxWidth(Double.MAX_VALUE);
        grid.add(filePathField, 1, 1);

        // 浏览按钮
        browseButton = new Button("浏览");
        browseButton.setOnAction(e -> browseFile());
        browseButton.setDisable(true);
        grid.add(browseButton, 2, 1);

        return grid;
    }


    private ScrollPane createDataDisplayArea() {
        dataContainer = new VBox(10);
        dataContainer.getStyleClass().add("data-container");
        ScrollPane scrollPane = new ScrollPane(dataContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(200);
        return scrollPane;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        previousButton = new Button("上一步");
        previousButton.setOnAction(e -> onPreviousButtonClick());
        previousButton.getStyleClass().add("nav-button");

        nextButton = new Button("下一步");
        nextButton.setOnAction(e -> onNextButtonClick());
        nextButton.getStyleClass().add("nav-button");

        buttonBox.getChildren().addAll(previousButton, nextButton);
        return buttonBox;
    }

    private void handleDataSourceSelection() {
        String selected = dataSourceComboBox.getValue();
        boolean enableFileBrowse = "Excel".equals(selected) || "纬地设计文件".equals(selected);
        filePathField.setDisable(!enableFileBrowse);
        browseButton.setDisable(!enableFileBrowse);

        dataContainer.getChildren().clear();
        if ("手动输入信息".equals(selected)) {
            setupManualInputFields();
        }
    }

    private void setupManualInputFields() {
        TextField nameField = new TextField();
        nameField.setPromptText("输入数据名称");
        TextField valueField = new TextField();
        valueField.setPromptText("输入数据值");
        Button addButton = new Button("添加数据");
        addButton.setOnAction(e -> addManualData(nameField, valueField));

        HBox inputBox = new HBox(10, nameField, valueField, addButton);
        dataContainer.getChildren().add(inputBox);
    }

    private void addManualData(TextField nameField, TextField valueField) {
        String name = nameField.getText().trim();
        String value = valueField.getText().trim();
        if (!name.isEmpty() && !value.isEmpty()) {
            Label dataLabel = new Label(name + ": " + value);
            dataContainer.getChildren().add(dataLabel);
            nameField.clear();
            valueField.clear();
        }
    }

    private void browseFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            // 这里可以添加读取文件内容的逻辑
        }
    }

    private void onPreviousButtonClick() {
        // 直接跳转到上一页，不进行任何验证
        MainController.getInstance().showPreviousView();
    }

    private void onNextButtonClick() {
        if (validateInputs()) {
            saveData();
            navigateToNextPage();
        } else {
            showErrorMessage("请选择数据来源并填写必要信息");
        }
    }

    private boolean validateInputs() {
        String selected = dataSourceComboBox.getValue();
        if (selected == null) return false;

        switch (selected) {
            case "Excel":
            case "纬地设计文件":
                return !filePathField.getText().trim().isEmpty();
            case "手动输入信息":
                return !dataContainer.getChildren().isEmpty();
            default:
                return false;
        }
    }

    private void saveData() {
        /*// 保存数据到 formData 对象
        formData.setDataSource(dataSourceComboBox.getValue());
        formData.setFilePath(filePathField.getText());
        // 如果是手动输入，可以将数据保存到一个列表中
        if ("手动输入信息".equals(dataSourceComboBox.getValue())) {
            List<String> manualData = new ArrayList<>();
            for (Node node : dataContainer.getChildren()) {
                if (node instanceof Label) {
                    manualData.add(((Label) node).getText());
                }
            }
            formData.setManualData(manualData);
        }*/
    }

    private void navigateToNextPage() {
        MainController.getInstance().showNextView();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("输入错误");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // 获取对话框的StyleClass
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/com/schdri/css/alter.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }
}