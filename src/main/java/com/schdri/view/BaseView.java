package com.schdri.view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public abstract class BaseView extends VBox {
    public BaseView() {
        this.setSpacing(10);
        this.getStyleClass().add("tab-content");
    }

    protected Label createTitleLabel(String title) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("content-title");
        return titleLabel;
    }
}