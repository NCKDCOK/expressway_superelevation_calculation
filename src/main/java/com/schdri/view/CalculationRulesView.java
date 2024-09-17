package com.schdri.view;

import com.schdri.controller.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CalculationRulesView extends BaseView {
    private Button previousButton;
    private Button nextButton;

    public CalculationRulesView() {
        super();
        this.getStylesheets().add(getClass().getResource("/com/schdri/css/calculationRules.css").toExternalForm());
        this.getStyleClass().add("calculation-rules-view");

        VBox mainContainer = new VBox(20);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = createTitleLabel("计算规则");
        titleLabel.getStyleClass().add("content-title");

        // 创建一个占位图片
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/schdri/img/image.png")));
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);

        // 创建一个StackPane来容纳图片，并添加边框和阴影效果
        StackPane imageContainer = new StackPane(imageView);
        imageContainer.getStyleClass().add("image-container");
        imageContainer.setAlignment(Pos.CENTER);

        // 添加导航按钮
        previousButton = new Button("上一步");
        previousButton.setOnAction(e -> onPreviousButtonClick());
        previousButton.getStyleClass().add("nav-button");

        nextButton = new Button("下一步");
        nextButton.setOnAction(e -> onNextButtonClick());
        nextButton.getStyleClass().add("nav-button");

        HBox buttonBox = new HBox(20, previousButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        mainContainer.getChildren().addAll(titleLabel, imageContainer, buttonBox);
        this.getChildren().add(mainContainer);
    }

    private void onPreviousButtonClick() {
        MainController.getInstance().showPreviousView();
    }

    private void onNextButtonClick() {
        MainController.getInstance().showNextView();
    }

    public Button getNextButton() {
        return nextButton;
    }
}