package com.schdri.domain;

import javafx.beans.property.*;
import lombok.Data;

@Data
public class FormData {
    // 第一个页面的字段
    private final StringProperty projectName = new SimpleStringProperty();
    private final ObjectProperty<String> projectType = new SimpleObjectProperty<>();
    private final StringProperty design = new SimpleStringProperty();
    private final StringProperty review = new SimpleStringProperty();
    private final StringProperty approval = new SimpleStringProperty();

    // 第二个页面的字段
    private final ObjectProperty<String> dataSource = new SimpleObjectProperty<>();
    private final StringProperty filePath = new SimpleStringProperty();

    // 第三个页面的字段（规则定义页面）
    private final ObjectProperty<Integer> speed = new SimpleObjectProperty<>();
    private final ObjectProperty<String> roadClass = new SimpleObjectProperty<>();
    private final ObjectProperty<String> roadType = new SimpleObjectProperty<>();
    private final ObjectProperty<String> rotationAxis = new SimpleObjectProperty<>();
    private final DoubleProperty laneWidth = new SimpleDoubleProperty();
    private final DoubleProperty laneSlope = new SimpleDoubleProperty();
    private final DoubleProperty hardShoulderWidth = new SimpleDoubleProperty();
    private final DoubleProperty hardShoulderSlope = new SimpleDoubleProperty();
    private final DoubleProperty softShoulderWidth = new SimpleDoubleProperty();
    private final DoubleProperty softShoulderSlope = new SimpleDoubleProperty();
    private final BooleanProperty hardShoulderSuperelevation = new SimpleBooleanProperty();
    private final BooleanProperty softShoulderSuperelevation = new SimpleBooleanProperty();
    private final ObjectProperty<Integer> maxSuperelevation = new SimpleObjectProperty<>();

    private StringProperty region = new SimpleStringProperty();

    // Getter 和 Setter 方法会由 Lombok 自动生成

    // 你可能还需要添加一些自定义方法，比如：
    public void clearBasicInfoView() {
        // 清除第一页数据
        projectName.set("");
        projectType.set(null);
        design.set("");
        review.set("");
        approval.set("");
    }

    public void clearDataInputView() {
        // 清除第二页数据
        dataSource.set(null);
        filePath.set("");
    }

    public void clearRuleDefinitionView() {

        // 清除第三页数据
        speed.set(null);
        roadClass.set(null);
        roadType.set(null);
        rotationAxis.set(null);
        laneWidth.set(0);
        laneSlope.set(0);
        hardShoulderWidth.set(0);
        hardShoulderSlope.set(0);
        softShoulderWidth.set(0);
        softShoulderSlope.set(0);
        hardShoulderSuperelevation.set(false);
        softShoulderSuperelevation.set(false);
        maxSuperelevation.set(null);
    }

    public void clearAll() {
        clearBasicInfoView();
        clearDataInputView();
        clearRuleDefinitionView();
    }


    //basicInfo方法
    public Property<String> projectNameProperty() {
        return projectName;
    }

    public Property<String> projectTypeProperty() {
        return projectType;
    }

    public Property<String> designProperty() {
        return design;
    }

    public Property<String> reviewProperty() {
        return review;
    }

    public Property<String> approvalProperty() {
        return approval;
    }


    //dataInput方法
    public Property<String> filePathProperty() {
        return filePath;
    }

    public Property<String> dataSourceProperty() {
        return dataSource;
    }


    //ruleDefinition方法
    public ObjectProperty<Integer> speedProperty() {
        return speed;
    }

    public ObjectProperty<String> roadClassProperty() {
        return roadClass;
    }

    public ObjectProperty<String> roadTypeProperty() {
        return roadType;
    }

    public ObjectProperty<String> rotationAxisProperty() {
        return rotationAxis;
    }

    public DoubleProperty laneWidthProperty() {
        return laneWidth;
    }

    public DoubleProperty laneSlopeProperty() {
        return laneSlope;
    }

    public DoubleProperty hardShoulderWidthProperty() {
        return hardShoulderWidth;
    }

    public DoubleProperty hardShoulderSlopeProperty() {
        return hardShoulderSlope;
    }

    public DoubleProperty softShoulderWidthProperty() {
        return softShoulderWidth;
    }

    public DoubleProperty softShoulderSlopeProperty() {
        return softShoulderSlope;
    }

    public BooleanProperty hardShoulderSuperelevationProperty() {
        return hardShoulderSuperelevation;
    }

    public BooleanProperty softShoulderSuperelevationProperty() {
        return softShoulderSuperelevation;
    }

    public ObjectProperty<Integer> maxSuperelevationProperty() {
        return maxSuperelevation;
    }

    public Property<String> regionProperty() {
        return region;
    }
}