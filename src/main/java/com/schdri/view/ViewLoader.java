package com.schdri.view;

import javafx.scene.Node;

public class ViewLoader {

    public static Node loadBasicInfoView() {
        return new BasicInfoView();
    }

    public static Node loadCalculationRulesView() {
        return new CalculationRulesView();
    }

    public static Node loadDataInputView() {
        return new DataInputView();
    }

    public static Node loadRuleDefinitionView() {
        return new RuleDefinitionView();
    }

    public static Node loadNextView(int currentViewIndex) {
        switch (currentViewIndex) {
            case 0:
                return loadBasicInfoView();
            case 1:
                return loadCalculationRulesView();
            case 2:
                return loadDataInputView();
            case 3:
                return loadRuleDefinitionView();
            default:
                return null; // 或者返回一个完成页面
        }
    }
}