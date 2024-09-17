package com.schdri.utils;

import com.schdri.domain.FormData;
import com.schdri.domain.WriteData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class CalculateUtil {

    // 常量定义
    private static final String S_CURVE = "S形曲线";
    private static final String C_CURVE = "C形曲线";
    private static final String REVERSE_CURVE = "回头曲线";
    private static final String OVAL_CURVE = "卵形曲线";

    private static final Double DECIMAL = 0.01;

    private static final int MAX_TRANSITION_FACTOR = 330;

    /**
     * 计算偏角和过渡长度
     *
     * @param dataList 数据列表
     */
    public static void calculateDeflectionAngles(List<WriteData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }



        FormData formData = FormDataManager.getInstance().getFormData();
        String str=GradientRateUtil.getGradientRate(formData.rotationAxisProperty().getValue(), formData.speedProperty().get().toString());
        log.info(str);
        int p = Integer.parseInt(str);
        double laneSlope = formData.laneSlopeProperty().doubleValue();
        double minTransitionFactor = formData.laneWidthProperty().doubleValue() * p;
        double maxTransitionFactor = formData.laneWidthProperty().doubleValue() * MAX_TRANSITION_FACTOR;

        //跳过第一行
        for (int i = 1; i < dataList.size(); i++) {
            WriteData currentData = dataList.get(i);
            if (isDataInvalid(currentData)) {
                continue;
            }

            // 跳过包含"半径＞350"的记录
            if (currentData.getSuperelevation().contains("半径＞350，不需设置超高")) {
                skipCurrentData(currentData);
                continue;
            }

            WriteData prevData = i > 0 ? dataList.get(i - 1) : null;
            WriteData nextData = i < dataList.size() - 1 ? dataList.get(i + 1) : null;

            calculateDeflectionAngles(currentData, prevData, nextData, laneSlope);
            calculateTransitionLengths(currentData, minTransitionFactor, maxTransitionFactor);
        }
    }

    /**
     * 跳过包含“半径＞350”记录的数据
     */
    private static void skipCurrentData(WriteData data) {
        data.setPrecedingDeflectionAngle("");
        data.setSucceedingDeflectionAngle("0");
        data.setMinPrecedingTransitionLength("0");
        data.setMaxPrecedingTransitionLength("0");
        data.setMinSucceedingTransitionLength("0");
        data.setMaxSucceedingTransitionLength("0");
        data.setSelectedPrecedingTransitionLength("0");
        data.setSelectedSucceedingTransitionLength("0");
    }

    /**
     * 检查数据是否无效
     */
    private static boolean isDataInvalid(WriteData data) {
        return data == null || data.getSuperelevation() == null || data.getSecond() == null
                || data.getFirstTransitionCurveLength() == null || data.getSecondTransitionCurveLength() == null;
    }

    /**
     * 计算前后偏角
     */
    private static void calculateDeflectionAngles(WriteData currentData, WriteData prevData, WriteData nextData,
                                                  double laneSlope) {
        double superElevation = safeParseDouble(currentData.getSuperelevation());
        double prevSuperElevation = prevData != null ? safeParseDouble(prevData.getSuperelevation()) : 0;
        double nextSuperElevation = nextData != null ? safeParseDouble(nextData.getSuperelevation()) : 0;

        currentData.setPrecedingDeflectionAngle(formatDouble(Double.parseDouble(calculateDeflectionAngle(currentData, superElevation,
                prevSuperElevation, true, laneSlope))));
        currentData.setSucceedingDeflectionAngle(formatDouble(Double.parseDouble(calculateDeflectionAngle(currentData, superElevation,
                nextSuperElevation, false, laneSlope))));
    }

    /**
     * 计算单个偏角
     */
    private static String calculateDeflectionAngle(WriteData data, double superElevation, double otherSuperElevation,
                                                   boolean isPreceding, double laneSlope) {
        String curveType = isPreceding ? "与前曲线成" : "与后曲线成";
        String transitionLengthField = isPreceding ? "FirstTransitionCurveLength" : "SecondTransitionCurveLength";
        String secondData = data.getSecond();

        if (secondData.contains(curveType + S_CURVE)) {
            return data.getSuperelevation();
        } else if (secondData.contains(curveType + C_CURVE)) {
            return String.valueOf(superElevation + Math.abs(superElevation - otherSuperElevation) / 2);
        } else if (secondData.contains(curveType + REVERSE_CURVE)) {
            return "0";
        } else if (secondData.contains(curveType + OVAL_CURVE)) {
            double transitionLength = safeParseDouble(getFieldValue(data, transitionLengthField));
            return transitionLength > 0 ? String.valueOf(Math.abs(superElevation - otherSuperElevation)) : "0";
        } else {
            return String.valueOf(superElevation + Math.abs(laneSlope));
        }
    }

    /**
     * 计算过渡长度
     */
    private static void calculateTransitionLengths(WriteData data, double minTransitionFactor,
                                                   double maxTransitionFactor) {
        double precedingDeflectionAngle = safeParseDouble(data.getPrecedingDeflectionAngle());
        double succeedingDeflectionAngle = safeParseDouble(data.getSucceedingDeflectionAngle());

        data.setMinPrecedingTransitionLength(
                formatDouble(precedingDeflectionAngle * DECIMAL * minTransitionFactor));
        data.setMaxPrecedingTransitionLength(
                formatDouble(precedingDeflectionAngle * DECIMAL * maxTransitionFactor));
        data.setMinSucceedingTransitionLength(
                formatDouble(succeedingDeflectionAngle * DECIMAL * minTransitionFactor));
        data.setMaxSucceedingTransitionLength(
                formatDouble(succeedingDeflectionAngle * DECIMAL * maxTransitionFactor));

        setSelectedTransitionLengths(data);
    }

    /**
     * 设置选定的过渡长度
     */
    private static void setSelectedTransitionLengths(WriteData data) {
        data.setSelectedPrecedingTransitionLength(calculateSelectedTransitionLength(
                data.getMinPrecedingTransitionLength(), data.getMaxPrecedingTransitionLength(),
                data.getFirstTransitionCurveLength()));

        data.setSelectedSucceedingTransitionLength(calculateSelectedTransitionLength(
                data.getMinSucceedingTransitionLength(), data.getMaxSucceedingTransitionLength(),
                data.getSecondTransitionCurveLength()));
    }

    /**
     * 计算选定的过渡长度
     */
    private static String calculateSelectedTransitionLength(String min, String max, String current) {
        double minValue = safeParseDouble(min);
        double maxValue = safeParseDouble(max);
        double currentValue = safeParseDouble(current);
        return formatDouble(Math.max(minValue, Math.min(currentValue, maxValue)));
    }

    /**
     * 安全地解析字符串为double
     */
    private static double safeParseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing double value: " + value);
            return 0.0;
        }
    }

    /**
     * 格式化double值为最多4位小数，并去掉不必要的零
     */
    private static String formatDouble(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.format("%.4f", value).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }

    /**
     * 通过反射获取字段值
     */
    private static String getFieldValue(WriteData data, String fieldName) {
        try {
            return (String) WriteData.class.getMethod("get" + fieldName).invoke(data);
        } catch (Exception e) {
            System.err.println("Error getting field value: " + e.getMessage());
            return "";
        }
    }
}
