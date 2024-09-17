package com.schdri.utils;

import com.schdri.domain.WriteData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class TransitionCalculatorUtil {
    static boolean f=false;

    public static void calculateTransitionPoints(List<WriteData> dataList) {

        //跳过第一行
        for(int i=1;i<dataList.size();i++){
            if(i==3)
                f=true;

            WriteData data = dataList.get(i);

            calculatePrecedingTransitionStartPoint(data);
            calculatePrecedingTransitionStartPointSuperelevation(data);
            calculateInsidePrecedingTransitionStartPoint(data);
            calculateNodeSettingPreceding(data);
            calculatePrecedingTransitionEndPoint(data);
            calculatePrecedingTransitionEndPointSuperelevation(data);

            calculateSucceedingTransitionEndPoint(data);
            calculateSucceedingTransitionEndPointSuperelevation(data);

            calculateSucceedingTransitionStartPoint(data);
            calculateSucceedingTransitionStartPointSuperelevation(data);
            calculateInsideSucceedingTransitionEndPoint(data);
            calculateNodeSettingSucceeding(data);

        }
    }

    private static double parseDoubleOrDefault(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 格式化double值为最多3位小数，并去掉不必要的零
     */
    private static String formatDouble(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.format("%.3f", value).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }

    private static void calculatePrecedingTransitionStartPoint(WriteData data) {



            double ak = parseDoubleOrDefault(data.getSelectedPrecedingTransitionLength(), 0);
            double m = parseDoubleOrDefault(data.getFirstTransitionCurveLength(), 0);
            double g = parseDoubleOrDefault(data.getFirstTransitionCurveStart(), 0);
            double l = parseDoubleOrDefault(data.getFirstStraightLength(), 0);
            double h = parseDoubleOrDefault(data.getFirstTransitionCurveEnd(), 0);


           // log.info("ak: " + ak + " m: " + m + " g: " + g + " l: " + l + " h: " + h);
            f = false;


            if (ak <= m) {
                data.setPrecedingTransitionStartPoint(formatDouble(g));

            } else if (ak > m && ak < (l + m)) {
                data.setPrecedingTransitionStartPoint(formatDouble(h - ak));

            } else if (ak >= (l + m)) {
                data.setPrecedingTransitionStartPoint(formatDouble(g + l));
            }

        
    }

    private static void calculatePrecedingTransitionStartPointSuperelevation(WriteData data) {
        String superelevation = data.getSuperelevation();
        String precedingDeflectionAngle = data.getPrecedingDeflectionAngle();

        // 检查输入是否为空或不是数字
        if (isNullOrEmpty(superelevation) || isNullOrEmpty(precedingDeflectionAngle) ||
                !isNumeric(superelevation) || !isNumeric(precedingDeflectionAngle)) {
            data.setPrecedingTransitionStartPointSuperelevation("-2");
            return;
        }

        // 如果都是有效数字，则进行计算
        try {
            double ad = Double.parseDouble(superelevation);
            double ae = Double.parseDouble(precedingDeflectionAngle);
            data.setPrecedingTransitionStartPointSuperelevation(formatDouble(ad - ae));
        } catch (NumberFormatException e) {
            // 以防万一还是捕获异常，虽然前面已经检查过了
            data.setPrecedingTransitionStartPointSuperelevation("-2");
        }
    }

    private static void calculateInsidePrecedingTransitionStartPoint(WriteData data) {
        double ak = parseDoubleOrDefault(data.getSelectedPrecedingTransitionLength(), 0);
        if (ak == 0) {
            data.setInsidePrecedingTransitionStartPoint("");
        } else {
            double an = parseDoubleOrDefault(data.getPrecedingTransitionStartPointSuperelevation(), 0);
            double ae = parseDoubleOrDefault(data.getPrecedingDeflectionAngle(), 0);
            double am = parseDoubleOrDefault(data.getPrecedingTransitionStartPoint(), 0);
            double result = (2 - an) / ae * ak + am;
            data.setInsidePrecedingTransitionStartPoint(formatDouble(result));
        }
    }

    private static void calculateNodeSettingPreceding(WriteData data) {
        if (data.getInsidePrecedingTransitionStartPoint() == null || data.getInsidePrecedingTransitionStartPoint().isEmpty()) {
            data.setNodeSettingPreceding("");
        } else {
            data.setNodeSettingPreceding("9999");
        }
    }

    private static void calculatePrecedingTransitionEndPoint(WriteData data) {
        double am = parseDoubleOrDefault(data.getPrecedingTransitionStartPoint(), 0);
        double ak = parseDoubleOrDefault(data.getSelectedPrecedingTransitionLength(), 0);
        data.setPrecedingTransitionEndPoint(formatDouble(am + ak));
    }

    private static void calculatePrecedingTransitionEndPointSuperelevation(WriteData data) {
        double an = parseDoubleOrDefault(data.getPrecedingTransitionStartPointSuperelevation(), 0);
        double ae = parseDoubleOrDefault(data.getPrecedingDeflectionAngle(), 0);
        data.setPrecedingTransitionEndPointSuperelevation(formatDouble(an + ae));
    }

    private static void calculateSucceedingTransitionStartPoint(WriteData data) {
        double aw = parseDoubleOrDefault(data.getSucceedingTransitionEndPoint(), 0);
        double al = parseDoubleOrDefault(data.getSelectedSucceedingTransitionLength(), 0);
        data.setSucceedingTransitionStartPoint(formatDouble(aw - al));
    }

    private static void calculateSucceedingTransitionStartPointSuperelevation(WriteData data) {
        data.setSucceedingTransitionStartPointSuperelevation(data.getPrecedingTransitionEndPointSuperelevation());
    }

    private static void calculateInsideSucceedingTransitionEndPoint(WriteData data) {
        double al = parseDoubleOrDefault(data.getSelectedSucceedingTransitionLength(), 0);
        if (al == 0) {
            data.setInsideSucceedingTransitionEndPoint("");
        } else {
            double aw = parseDoubleOrDefault(data.getSucceedingTransitionEndPoint(), 0);
            double ax = parseDoubleOrDefault(data.getSucceedingTransitionEndPointSuperelevation(), 0);
            double ah = parseDoubleOrDefault(data.getSucceedingDeflectionAngle(), 0);
            double result = aw - (2 - ax) / ah * al;
            data.setInsideSucceedingTransitionEndPoint(formatDouble(result));
        }
    }

    private static void calculateNodeSettingSucceeding(WriteData data) {
        if (data.getInsideSucceedingTransitionEndPoint() == null || data.getInsideSucceedingTransitionEndPoint().isEmpty()) {
            data.setNodeSettingSucceeding("");
        } else {
            data.setNodeSettingSucceeding("9999");
        }
    }

    private static void calculateSucceedingTransitionEndPoint(WriteData data) {
        double al = parseDoubleOrDefault(data.getSelectedSucceedingTransitionLength(), 0);
        double o = parseDoubleOrDefault(data.getSecondTransitionCurveLength(), 0);
        double k = parseDoubleOrDefault(data.getSecondTransitionCurveEnd(), 0);
        double p = parseDoubleOrDefault(data.getSecondStraightLength(), 0);
        double j = parseDoubleOrDefault(data.getSecondTransitionCurveStart(), 0);

        if (al <= o) {
            data.setSucceedingTransitionEndPoint(formatDouble(k));
        } else if (al > o && al < (p + o)) {
            data.setSucceedingTransitionEndPoint(formatDouble(j + al));
        } else if (al >= (p + o)) {
            data.setSucceedingTransitionEndPoint(formatDouble(k + p));
        }
    }

    private static void calculateSucceedingTransitionEndPointSuperelevation(WriteData data) {
        String superelevation = data.getSuperelevation();
        String succeedingDeflectionAngle = data.getSucceedingDeflectionAngle();

// 检查输入是否为空或不是数字
        if (isNullOrEmpty(superelevation) || isNullOrEmpty(succeedingDeflectionAngle) ||
                !isNumeric(superelevation) || !isNumeric(succeedingDeflectionAngle)) {
            data.setSucceedingTransitionEndPointSuperelevation("-2");
            return;
        }

// 如果都是有效数字，则进行计算
        try {
            double ad = Double.parseDouble(superelevation);
            double ah = Double.parseDouble(succeedingDeflectionAngle);
            data.setSucceedingTransitionEndPointSuperelevation(formatDouble(ad - ah));
        } catch (NumberFormatException e) {
            // 以防万一还是捕获异常，虽然前面已经检查过了
            data.setSucceedingTransitionEndPointSuperelevation("-2");
        }
    }

    // 辅助方法：检查字符串是否为空或只包含空白字符
    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // 辅助方法：检查字符串是否可以解析为数字
    private static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}