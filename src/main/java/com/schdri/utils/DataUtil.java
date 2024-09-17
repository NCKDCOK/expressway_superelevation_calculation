package com.schdri.utils;

import com.schdri.domain.ReadData;
import com.schdri.domain.WriteData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataUtil {

    public static List<ReadData> getReadDataList(List<ReadData> dataList) {
        return processData(dataList);
    }

    public static List<String> getLast(List<ReadData> dataList) {
        List<String> lastList = new ArrayList<>();
        for (ReadData data : dataList) {
            if (data.getStraightLineLength() != null) {
                lastList.add(data.getStraightLineLength());
            }
        }
        return lastList;
    }

    public static List<ReadData> processData(List<ReadData> dataList) {
        List<String> lastList = getLast(dataList);
        List<ReadData> mergedList = new ArrayList<>();
        int x = 0;
        int dataSize = dataList.size();

        for (int i = 0; i < dataSize; i += 2) {
            ReadData topRow = dataList.get(i);
            ReadData bottomRow = (i + 1 < dataSize) ? dataList.get(i + 1) : null;
            ReadData mergedData = new ReadData();

            if (bottomRow == null) {
                // 如果bottomRow为空，说明这是最后一条记录且没有配对的记录
                mergedData.setIntersectionNumber(topRow.getIntersectionNumber());
                mergedData.setNorthCoordinate(topRow.getNorthCoordinate());
                mergedData.setEastCoordinate(topRow.getEastCoordinate());
                mergedData.setStakeNumber(removeKPrefix(topRow.getStakeNumber()));
                mergedData.setDeflectionAngle(topRow.getDeflectionAngle());
                mergedData.setRadius(topRow.getRadius());
                mergedData.setFirstTransitionCurveStart(removeKPrefix(topRow.getFirstTransitionCurveStart()));
                mergedData.setFirstTransitionCurveEnd(removeKPrefix(topRow.getFirstTransitionCurveEnd()));
                mergedData.setCurveMidpoint(removeKPrefix(topRow.getCurveMidpoint()));
                mergedData.setSecondTransitionCurveStart(removeKPrefix(topRow.getSecondTransitionCurveStart()));
                mergedData.setSecondTransitionCurveEnd(removeKPrefix(topRow.getSecondTransitionCurveEnd()));
            } else {
                // 合并数据，处理 null 值
                mergeData(mergedData, topRow, bottomRow);
            }

            // 处理最后一列的数据
            if (i != 0 && x < lastList.size()) {
                mergedData.setStraightLineLength(lastList.get(x));
                x++;
            }

            mergedList.add(mergedData);
        }

        return mergedList;
    }

    private static void mergeData(ReadData mergedData, ReadData topRow, ReadData bottomRow) {
        mergedData.setIntersectionNumber(mergeStrings(topRow.getIntersectionNumber(), bottomRow.getIntersectionNumber()));
        mergedData.setNorthCoordinate(mergeStrings(topRow.getNorthCoordinate(), bottomRow.getNorthCoordinate()));
        mergedData.setEastCoordinate(mergeStrings(topRow.getEastCoordinate(), bottomRow.getEastCoordinate()));
        mergedData.setStakeNumber(removeKPrefix(mergeStrings(topRow.getStakeNumber(), bottomRow.getStakeNumber())));
        mergedData.setDeflectionAngle(mergeStrings(topRow.getDeflectionAngle(), bottomRow.getDeflectionAngle()));
        mergedData.setRadius(mergeStrings(topRow.getRadius(), bottomRow.getRadius()));



        mergedData.setFirstTransitionCurveEnd(removeKPrefix(mergeStrings(topRow.getFirstTransitionCurveEnd(), bottomRow.getFirstTransitionCurveEnd())));

        //单独处理
        String s =  mergeStrings(topRow.getFirstTransitionCurveStart(), bottomRow.getFirstTransitionCurveStart());
        // 使用 isNullOrBlank 方法来检查字符串
        if (isNullOrBlank(s)) {
            mergedData.setFirstTransitionCurveStart(mergedData.getFirstTransitionCurveEnd());
        } else {
            mergedData.setFirstTransitionCurveStart(removeKPrefix(s));
        }

        mergedData.setCurveMidpoint(removeKPrefix(mergeStrings(topRow.getCurveMidpoint(), bottomRow.getCurveMidpoint())));
        mergedData.setSecondTransitionCurveStart(removeKPrefix(mergeStrings(topRow.getSecondTransitionCurveStart(), bottomRow.getSecondTransitionCurveStart())));

        // 单独处理
        String str = mergeStrings(topRow.getSecondTransitionCurveEnd(), bottomRow.getSecondTransitionCurveEnd());

        // 使用 isNullOrBlank 方法来检查字符串
        if (isNullOrBlank(str)) {
            mergedData.setSecondTransitionCurveEnd(mergedData.getSecondTransitionCurveStart());
        } else {
            mergedData.setSecondTransitionCurveEnd(removeKPrefix(str));
        }
    }


    // 辅助方法
    private static boolean isNullOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String mergeStrings(String s1, String s2) {
        String result = (s1 != null ? s1.trim() : "") + (s2 != null ? s2.trim() : "");
        return result.isEmpty() ? null : result; // 如果结果为空字符串，返回 null
    }

    private static String removeKPrefix(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        if (value.startsWith("K")) {
            String[] parts = value.split("\\+");
            if (parts.length == 2) {
                try {
                    double km = Double.parseDouble(parts[0].substring(1));
                    double meters = Double.parseDouble(parts[1]);
                    double result = km * 1000 + meters;
                    return String.format("%.3f", result);
                } catch (NumberFormatException e) {
                    // 如果解析失败，返回原始值
                }
            }
        }

        return value;
    }

    public static List<WriteData> getWriteDataList(List<ReadData> readDataList) {
        List<WriteData> writeDataList = new ArrayList<>();

        for (int i = 0; i < readDataList.size(); i++) {
            ReadData readData = readDataList.get(i);
            WriteData writeData = new WriteData();
            try {
                BeanUtils.copyProperties(writeData, readData);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            // 处理需要计算的字段
            String firstTransitionCurveStartStr = readData.getFirstTransitionCurveStart();
            String firstTransitionCurveEndStr = readData.getFirstTransitionCurveEnd();
            String secondTransitionCurveStartStr = readData.getSecondTransitionCurveStart();
            String secondTransitionCurveEndStr = readData.getSecondTransitionCurveEnd();

            // 处理第一过渡曲线长度
            if (firstTransitionCurveStartStr == null || firstTransitionCurveStartStr.trim().isEmpty()) {
                writeData.setFirstTransitionCurveLength("0");
            } else {
                Double start = toDouble(firstTransitionCurveStartStr);
                Double end = toDouble(firstTransitionCurveEndStr);
                if (start != null && end != null) {
                    double length = end - start;
                    writeData.setFirstTransitionCurveLength(formatNumber(length));
                } else {
                    writeData.setFirstTransitionCurveLength("0");
                }
            }

            // 处理第二过渡曲线长度
            if (secondTransitionCurveEndStr == null || secondTransitionCurveEndStr.trim().isEmpty()) {
                writeData.setSecondTransitionCurveLength("0");
            } else {
                Double start = toDouble(secondTransitionCurveStartStr);
                Double end = toDouble(secondTransitionCurveEndStr);
                if (start != null && end != null) {
                    double length = end - start;
                    writeData.setSecondTransitionCurveLength(formatNumber(length));
                } else {
                    writeData.setSecondTransitionCurveLength("0");
                }
            }

            // 处理曲线长度
            if (secondTransitionCurveStartStr != null && !secondTransitionCurveStartStr.trim().isEmpty() &&
                    firstTransitionCurveEndStr != null && !firstTransitionCurveEndStr.trim().isEmpty()) {
                Double start = toDouble(firstTransitionCurveEndStr);
                Double end = toDouble(secondTransitionCurveStartStr);
                if (start != null && end != null) {
                    double length = end - start;
                    writeData.setCurveLength(formatNumber(length));
                }
            }

            if (readData.getDeflectionAngle() != null) {
                if (readData.getDeflectionAngle().contains("Y")) {
                    writeData.setDirection("右");
                } else if (readData.getDeflectionAngle().contains("Z")) {
                    writeData.setDirection("左");
                }
            }

            // 计算 firstStraightLength
            if (i > 0) {
                ReadData previousData = readDataList.get(i - 1);
                writeData.setFirstStraightLength(formatNumber(calculateStraightLength(
                        previousData.getSecondTransitionCurveEnd(),
                        readData.getFirstTransitionCurveStart(),
                        readData.getFirstTransitionCurveEnd(),
                        previousData.getSecondTransitionCurveStart()
                )));
            }

            // 计算 secondStraightLength
            if (i < readDataList.size() - 1) {
                ReadData nextData = readDataList.get(i + 1);
                writeData.setSecondStraightLength(formatNumber(calculateStraightLength(
                        readData.getSecondTransitionCurveEnd(),
                        nextData.getFirstTransitionCurveStart(),
                        nextData.getFirstTransitionCurveEnd(),
                        readData.getSecondTransitionCurveStart()
                )));
            }

            // 处理字符串中的前导零和负号
            normalizeWriteDataFields(writeData);

            writeDataList.add(writeData);
        }

        return writeDataList;
    }

    public static Double toDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String normalizeString(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        // 移除所有非数字字符（除了小数点和负号）
        value = value.replaceAll("[^0-9.-]", "");

        // 处理负数
        boolean isNegative = value.startsWith("-");
        if (isNegative) {
            value = value.substring(1);
        }

        // 移除前导零
        value = value.replaceFirst("^0+(?!$)", "");

        // 如果有小数点，移除尾部的零和不必要的小数点
        if (value.contains(".")) {
            value = value.replaceAll("0+$", "").replaceAll("\\.$", "");
        }

        // 如果结果为空（比如原字符串是".000"），返回"0"
        if (value.isEmpty()) {
            return "0";
        }

        // 重新添加负号（如果原来是负数）
        return isNegative ? "-" + value : value;
    }

    private static void normalizeWriteDataFields(WriteData writeData) {
        writeData.setNorthCoordinate(normalizeString(writeData.getNorthCoordinate()));
        writeData.setEastCoordinate(normalizeString(writeData.getEastCoordinate()));
        writeData.setStakeNumber(normalizeString(writeData.getStakeNumber()));
        writeData.setDeflectionAngle(normalizeString(writeData.getDeflectionAngle()));
        writeData.setRadius(normalizeString(writeData.getRadius()));

        writeData.setFirstTransitionCurveStart(normalizeString(writeData.getFirstTransitionCurveStart()));
        writeData.setFirstTransitionCurveEnd(normalizeString(writeData.getFirstTransitionCurveEnd()));
        writeData.setCurveMidpoint(normalizeString(writeData.getCurveMidpoint()));
        writeData.setSecondTransitionCurveStart(normalizeString(writeData.getSecondTransitionCurveStart()));
        writeData.setSecondTransitionCurveEnd(normalizeString(writeData.getSecondTransitionCurveEnd()));
    }

    private static String formatNumber(Double number) {
        if (number == null) {
            return "0";
        }
        if (number == number.longValue()) {
            return String.format("%d", number.longValue());
        }
        return String.format("%.2f", number);
    }

    private static double calculateStraightLength(String k, String g, String h, String j) {
        if(toDouble(j)==null||toDouble(h)==null||toDouble(g)==null){
            return 0;
        }

        double baseValue = g.isEmpty() ? toDouble(h) : toDouble(g);

        if (k.isEmpty()) {
            return baseValue - toDouble(j);
        } else {
            return baseValue - toDouble(k);
        }
    }
}