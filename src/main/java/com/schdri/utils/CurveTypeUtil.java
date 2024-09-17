package com.schdri.utils;

import com.schdri.domain.WriteData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class CurveTypeUtil {
    private static final int ZERO = 0;
    private static final Double ZERO_DOUBLE = 0.0;

    public static void determineCurveTypes(List<WriteData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // 跳过第一行
        for (int i = 1; i < dataList.size(); i++) {
            WriteData currentCurve = dataList.get(i);
            WriteData nextCurve = (i < dataList.size() - 1) ? dataList.get(i + 1) : null;
            //WriteData prevCurve = (i > 0) ? dataList.get(i - 1) : null;

            if (i == 9) {
                log.info(currentCurve.toString());
                log.info(nextCurve.toString());
            }

            String firstJudgment = firstJudgment(currentCurve);
            currentCurve.setFirst(firstJudgment);

            String secondJudgment = secondJudgment(currentCurve, nextCurve);
            currentCurve.setSecond(currentCurve.getSecond()+secondJudgment);

            String curveType = combineJudgments(firstJudgment, secondJudgment);
            currentCurve.setCurveType(curveType);
        }
    }

    private static String firstJudgment(WriteData curve) {
        double firstTransitionLength = parseDouble(curve.getFirstTransitionCurveLength());
        double mainCurveLength = parseDouble(curve.getCurveLength());
        double secondTransitionLength = parseDouble(curve.getSecondTransitionCurveLength());

        if (firstTransitionLength == ZERO_DOUBLE && mainCurveLength > ZERO_DOUBLE && secondTransitionLength == ZERO_DOUBLE) {
            return "单圆曲线";
        } else if (firstTransitionLength > ZERO_DOUBLE && mainCurveLength == ZERO_DOUBLE && secondTransitionLength > ZERO_DOUBLE) {
            return "凸型曲线";
        } else if (firstTransitionLength > ZERO_DOUBLE && mainCurveLength > ZERO_DOUBLE && secondTransitionLength > ZERO_DOUBLE) {
            return "普通曲线";
        }
        return "";
    }

    private static String secondJudgment(WriteData currentCurve, WriteData nextCurve) {

        String result = "";
        boolean foundMatch = false;

        // 首先检查当前曲线是否有有效数据
        if (isEmptyCurve(currentCurve)) {
            return result;
        }

        double m = parseDouble(currentCurve.getFirstTransitionCurveLength());
        double n = parseDouble(currentCurve.getCurveLength());
        double o = parseDouble(currentCurve.getSecondTransitionCurveLength());
        double p = parseDouble(currentCurve.getSecondStraightLength());

        // 判断“与后曲线成S形曲线”
        if (!foundMatch && p == ZERO_DOUBLE && nextCurve != null && !isEmptyCurve(nextCurve)) {
            boolean isOppositeDirection = !safeEquals(currentCurve.getDirection(), nextCurve.getDirection());
            if (isOppositeDirection) {
                result = "与后曲线成S形曲线";
                foundMatch = true;
            }
        }

        // 判断“与后曲线成C形曲线”
        if (!foundMatch && o > ZERO_DOUBLE && p == ZERO_DOUBLE && nextCurve != null && !isEmptyCurve(nextCurve)) {
            double nextM = parseDouble(nextCurve.getFirstTransitionCurveLength());
            boolean isSameDirection = safeEquals(currentCurve.getDirection(), nextCurve.getDirection());
            if (nextM > ZERO_DOUBLE && isSameDirection) {
                result = "与后曲线成C形曲线";
                foundMatch = true;
            }
        }

        // 判断“与后曲线成卵形曲线”
        if (!foundMatch && m > ZERO_DOUBLE && n > ZERO_DOUBLE && p == ZERO_DOUBLE && nextCurve != null && !isEmptyCurve(nextCurve)) {
            double nextL = parseDouble(nextCurve.getFirstStraightLength());
            double nextN = parseDouble(nextCurve.getCurveLength());
            double nextO = parseDouble(nextCurve.getSecondTransitionCurveLength());
            double nextM = parseDouble(nextCurve.getFirstTransitionCurveLength());
            boolean isSameDirection = safeEquals(currentCurve.getDirection(), nextCurve.getDirection());
            if (nextL == ZERO_DOUBLE && nextN > ZERO_DOUBLE && nextO > ZERO_DOUBLE && isSameDirection &&
                    ((o > ZERO_DOUBLE && nextM == ZERO_DOUBLE) || (o == ZERO_DOUBLE && nextM > ZERO_DOUBLE))) {
                result = "与后曲线成卵形曲线";
                foundMatch = true;
            }
        }

        // 判断“与后曲线成回头曲线”
        if (!foundMatch && m > ZERO_DOUBLE && n > ZERO_DOUBLE && o == ZERO_DOUBLE && p == ZERO_DOUBLE && nextCurve != null && !isEmptyCurve(nextCurve)) {
            double nextL = parseDouble(nextCurve.getFirstStraightLength());
            double nextM = parseDouble(nextCurve.getFirstTransitionCurveLength());
            double nextN = parseDouble(nextCurve.getCurveLength());
            double nextO = parseDouble(nextCurve.getSecondTransitionCurveLength());
            boolean isSameDirection = safeEquals(currentCurve.getDirection(), nextCurve.getDirection());
            boolean isSameRadius = safeEquals(currentCurve.getRadius(), nextCurve.getRadius());
            if (nextL == ZERO_DOUBLE && nextM == ZERO_DOUBLE && nextN > ZERO_DOUBLE && nextO > ZERO_DOUBLE && isSameDirection && isSameRadius) {
                result = "与后曲线成回头曲线";
                foundMatch = true;
            }
        }


        if (result.contains("与后曲线成")) {
            // 获取当前曲线中 "与后曲线成" 后面的类型描述
            String currentType = result.substring(result.indexOf("与后曲线成") + "与后曲线成".length());

            // 如果 nextCurve 不为空且不为空曲线
            if (nextCurve != null && !isEmptyCurve(nextCurve)) {
                // 设置 nextCurve 的 Second 属性为 "与前曲线成" + currentType
                nextCurve.setSecond("与前曲线成" + currentType);
            }
        }else{
            if (nextCurve != null) {
                nextCurve.setSecond("");
            }
        }



        return result;
    }



    // 新增一个辅助方法来检查曲线是否为空
    private static boolean isEmptyCurve(WriteData curve) {
        return curve == null ||
                (isEmpty(curve.getFirstTransitionCurveLength()) &&
                        isEmpty(curve.getCurveLength()) &&
                        isEmpty(curve.getSecondTransitionCurveLength()) &&
                        isEmpty(curve.getSecondStraightLength()) &&
                        isEmpty(curve.getDirection()) &&
                        isEmpty(curve.getRadius()));
    }

    // 辅助方法来检查字符串是否为空
    private static boolean isEmpty(String s) {
        if (s == null || s.trim().isEmpty()) {
            return true;
        }
        try {
            return Double.parseDouble(s.trim()) == 0;
        } catch (NumberFormatException e) {
            return false; // 如果不能解析为数字，就不认为是0
        }
    }

    private static String combineJudgments(String firstJudgment, String secondJudgment) {
        if (!secondJudgment.isEmpty()) {
            return secondJudgment;
        } else {
            return firstJudgment;
        }
    }

    private static double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static boolean safeEquals(String s1, String s2) {
        return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
    }
}