package com.schdri.view;

import com.schdri.controller.MainController;
import com.schdri.domain.FormData;
import com.schdri.utils.FormDataManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Data;


@Data
public class BasicInfoView extends BaseView {
    private FormData formData;

    private TextField projectNameField;
    private ComboBox<String> projectTypeComboBox;
    private TextField designField;
    private TextField reviewField;
    private TextField approvalField;
    private Button nextButton;

    public BasicInfoView() {
        super();
        this.formData = FormDataManager.getInstance().getFormData();

        this.getStylesheets().add(getClass().getResource("/com/schdri/css/basicInfo.css").toExternalForm());
        this.getStyleClass().add("basic-info-view");

        initializeUI();
        bindFormData();
    }

    private void initializeUI() {
        Label titleLabel = createTitleLabel("基本信息输入");
        titleLabel.getStyleClass().add("content-title");

        VBox contentBox = new VBox(20);
        contentBox.getStyleClass().add("content-box");
        contentBox.getChildren().addAll(
                createFormRow("项目名称:", projectNameField = new TextField()),
                createFormRow("项目类型:", projectTypeComboBox = new ComboBox<>()),
                createFormRow("设计:", designField = new TextField()),
                createFormRow("复核:", reviewField = new TextField()),
                createFormRow("审核:", approvalField = new TextField())
        );

        projectTypeComboBox.getItems().addAll("设计", "咨询");
        projectTypeComboBox.setPromptText("选择项目类型");

        nextButton = new Button("下一步");
        nextButton.setOnAction(e -> onNextButtonClick());
        nextButton.getStyleClass().add("next-button");

        HBox buttonBox = new HBox(nextButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        VBox mainContainer = new VBox(20, titleLabel, contentBox, buttonBox);
        mainContainer.getStyleClass().add("main-container");

        this.getChildren().add(mainContainer);
        this.setPadding(new Insets(30));
    }

    private HBox createFormRow(String labelText, Control control) {
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");

        HBox row = new HBox(20, label, control);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("form-row");

        control.getStyleClass().add("form-control");
        HBox.setHgrow(control, Priority.ALWAYS);

        return row;
    }

    private void bindFormData() {
        projectNameField.textProperty().bindBidirectional(formData.projectNameProperty());
        projectTypeComboBox.valueProperty().bindBidirectional(formData.projectTypeProperty());
        designField.textProperty().bindBidirectional(formData.designProperty());
        reviewField.textProperty().bindBidirectional(formData.reviewProperty());
        approvalField.textProperty().bindBidirectional(formData.approvalProperty());
    }

    private void onNextButtonClick() {
        if (validateInputs()) {
            navigateToNextPage();
        } else {
            showErrorMessage("请填写所有必填字段");
        }
    }

    private boolean validateInputs() {
        return projectNameField != null && projectNameField.getText() != null && !projectNameField.getText().trim().isEmpty() &&
                projectTypeComboBox != null && projectTypeComboBox.getValue() != null &&
                designField != null && designField.getText() != null && !designField.getText().trim().isEmpty() &&
                reviewField != null && reviewField.getText() != null && !reviewField.getText().trim().isEmpty() &&
                approvalField != null && approvalField.getText() != null && !approvalField.getText().trim().isEmpty();
    }

    private void setErrorStyle(Control control) {
        control.getStyleClass().add("error");
    }

    private void removeErrorStyle(Control control) {
        control.getStyleClass().remove("error");
    }

    private void navigateToNextPage() {
        MainController.getInstance().showNextView();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("输入错误");
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/com/schdri/css/alter.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }
}