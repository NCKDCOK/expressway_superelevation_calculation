package com.schdri.controller;

import com.schdri.domain.FormData;
import com.schdri.view.ViewLoader;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController {
    private static MainController instance;

    public static final int BASIC_INFO_VIEW_INDEX = 0;
    public static final int CALCULATION_RULES_VIEW_INDEX = 1;
    public static final int DATA_INPUT_VIEW_INDEX = 2;
    public static final int RULE_DEFINITION_VIEW_INDEX = 3;

    @FXML private StackPane contentArea;
    @FXML private Button basicInfoButton;
    @FXML private Button calculationRulesButton;
    @FXML private Button dataInputButton;
    @FXML private Button ruleDefinitionButton;
    @FXML private Button settingsButton;
    @FXML private VBox navigationBar;

    private Button currentSelectedButton;
    private FormData formData = new FormData();
    private int currentViewIndex = 0;

    public static MainController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MainController has not been initialized");
        }
        return instance;
    }

    @FXML
    private void initialize() {
        instance = this;
        currentSelectedButton = basicInfoButton;
        setSelectedButton(currentSelectedButton);
        setContent(showBasicInfoView());
    }

    @FXML
    private void onBasicInfoButtonClicked() {
        showView(BASIC_INFO_VIEW_INDEX);
    }

    @FXML
    private void onCalculationRulesButtonClicked() {
        showView(CALCULATION_RULES_VIEW_INDEX);
    }

    @FXML
    private void onDataInputButtonClicked() {
        showView(DATA_INPUT_VIEW_INDEX);
    }

    @FXML
    private void onRuleDefinitionButtonClicked() {
        showView(RULE_DEFINITION_VIEW_INDEX);
    }

    @FXML
    private void onSettingsButtonClicked() {
        // TODO: Implement settings functionality
    }

    @FXML
    private void onNextButtonClicked() {
        showNextView();
    }

    public void showView(int targetViewIndex) {
        if (targetViewIndex >= 0 && targetViewIndex <= RULE_DEFINITION_VIEW_INDEX) {
            currentViewIndex = targetViewIndex;
            Node content = null;
            switch (currentViewIndex) {
                case BASIC_INFO_VIEW_INDEX:
                    content = showBasicInfoView();
                    setSelectedButton(basicInfoButton);
                    break;
                case CALCULATION_RULES_VIEW_INDEX:
                    content = showCalculationRulesView();
                    setSelectedButton(calculationRulesButton);
                    break;
                case DATA_INPUT_VIEW_INDEX:
                    content = showDataInputView();
                    setSelectedButton(dataInputButton);
                    break;
                case RULE_DEFINITION_VIEW_INDEX:
                    content = showRuleDefinitionView();
                    setSelectedButton(ruleDefinitionButton);
                    break;
            }
            setContent(content);
        } else {
            handleFinalStep();
        }
    }

    public void showNextView() {
        showView(currentViewIndex + 1);
    }

    public void showPreviousView() {
        showView(currentViewIndex - 1);
    }

    private Node showBasicInfoView() {
        return ViewLoader.loadBasicInfoView();
    }

    private Node showCalculationRulesView() {
        return ViewLoader.loadCalculationRulesView();
    }

    private Node showDataInputView() {
        return ViewLoader.loadDataInputView();
    }

    private Node showRuleDefinitionView() {
        return ViewLoader.loadRuleDefinitionView();
    }

    /*public void setContent(Node content) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(content);
    }

    private void setSelectedButton(Button selectedButton) {
        if (currentSelectedButton != null) {
            currentSelectedButton.getStyleClass().remove("selected");
        }
        selectedButton.getStyleClass().add("selected");
        currentSelectedButton = selectedButton;
    }*/


    private void setContent(Node content) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), contentArea);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished((e) -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentArea);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void setSelectedButton(Button selectedButton) {
        if (currentSelectedButton != null) {
            currentSelectedButton.getStyleClass().remove("selected");
        }
        selectedButton.getStyleClass().add("selected");
        currentSelectedButton = selectedButton;

        // 更新过渡区域的背景色
        VBox transitionBox = (VBox) navigationBar.getParent().lookup(".transition-box");
        if (transitionBox != null) {
            transitionBox.setStyle("-fx-background-color: linear-gradient(to left, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.02));");
        }
    }

    private void handleFinalStep() {
        // 处理最后一步，比如显示总结信息或提交数据
        System.out.println("Final step reached. Implement your logic here.");
    }

    public FormData getFormData() {
        return formData;
    }

    public int getCurrentViewIndex() {
        return currentViewIndex;
    }
}