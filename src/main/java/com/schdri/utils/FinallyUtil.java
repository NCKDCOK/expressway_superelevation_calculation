package com.schdri.utils;

import com.schdri.domain.FinallyData;
import com.schdri.domain.OutPutData;
import com.schdri.domain.WriteData;

import java.util.ArrayList;
import java.util.List;


/*
 *leftEarthShoulder; // 左侧土路肩
 * leftHardShoulder; // 左侧硬路肩
 * leftDrivingLane; // 左侧行车道
 * rightDrivingLane; // 右侧行车道
 * rightHardShoulder; // 右侧硬路肩
 * stationNumber; // 桩号
 *
 * 在桩号：insidePrecedingTransitionStartPoint; 前渐变段内侧起点和insideSucceedingTransitionEndPoint; 后渐变段内侧终点
 * 若deflectionAngle="左"时，左侧值为-2，右侧值为9999反之左侧值为9999，右侧值为-2
 *
 * 其余桩号遵循超高值>0,若deflectionAngle=“左”，则左侧为负，右侧正常，反之左侧正常，右侧为负
 *
 * 其余桩号及对应超高值：
 * {precedingTransitionStartPoint，precedingTransitionStartPointSuperelevation}
 * {precedingTransitionEndPoint，precedingTransitionEndPointSuperelevation}
 * {succeedingTransitionStartPoint，succeedingTransitionStartPointSuperelevation}
 * {succeedingTransitionEndPoint，succeedingTransitionEndPointSuperelevation}
 *
 *
 * */

public class FinallyUtil {

    private static final String TWO = "-2";
    private static final String NINE = "9999";

    public static List<OutPutData> finallyCalculate(List<WriteData> dataList) {
        List<OutPutData> outPutDataList = new ArrayList<>();

        for (int i = 1; i < dataList.size(); i++) {
            WriteData data = dataList.get(i);   //跳过第一行


            OutPutData outPutData = new OutPutData();
            List<FinallyData> finallyDataList = new ArrayList<>();

            outPutData.setIntersectionNumber(data.getIntersectionNumber());





            // 处理前渐变段起点
            finallyDataList.add(createFinallyData(data.getPrecedingTransitionStartPoint(), data.getDirection(), false, data.getPrecedingTransitionStartPointSuperelevation()));

            // 处理前渐变段内侧起点
            finallyDataList.add(createFinallyData(data.getInsidePrecedingTransitionStartPoint(), data.getDirection(), true));

            //处理前渐变段终点
            finallyDataList.add(createFinallyData(data.getPrecedingTransitionEndPoint(), data.getDirection(), false, data.getPrecedingTransitionEndPointSuperelevation()));

            //处理后渐变段起点
            finallyDataList.add(createFinallyData(data.getSucceedingTransitionStartPoint(), data.getDirection(), false, data.getSucceedingTransitionStartPointSuperelevation()));

            // 处理后渐变段内侧终点
            finallyDataList.add(createFinallyData(data.getInsideSucceedingTransitionEndPoint(), data.getDirection(), true));

            //处理后渐变段终点
            finallyDataList.add(createFinallyData(data.getSucceedingTransitionEndPoint(), data.getDirection(), false, data.getSucceedingTransitionEndPointSuperelevation()));

            outPutData.setFinallyDataList(finallyDataList);
            outPutDataList.add(outPutData);
        }

        return outPutDataList;
    }

    private static FinallyData createFinallyData(String stationNumber, String deflectionAngle, boolean isTransitionPoint) {
        return createFinallyData(stationNumber, deflectionAngle, isTransitionPoint, null);
    }

    private static FinallyData createFinallyData(String stationNumber, String direction, boolean isTransitionPoint, String superelevation) {
        FinallyData finallyData = new FinallyData();
        finallyData.setStationNumber(stationNumber);

        if (isTransitionPoint) {
            if ("左".equals(direction)) {
                finallyData.setLeftEarthShoulder(TWO);
                finallyData.setLeftHardShoulder(TWO);
                finallyData.setLeftDrivingLane(TWO);
                finallyData.setRightDrivingLane(NINE);
                finallyData.setRightHardShoulder(NINE);
                finallyData.setRightEarthShoulder(NINE);
            } else {
                finallyData.setLeftEarthShoulder(NINE);
                finallyData.setLeftHardShoulder(NINE);
                finallyData.setLeftDrivingLane(NINE);
                finallyData.setRightDrivingLane(TWO);
                finallyData.setRightHardShoulder(TWO);
                finallyData.setRightEarthShoulder(TWO);

            }
        } else if (superelevation != null) {
            double superElevationValue = Double.parseDouble(superelevation);
            if (superElevationValue > 0) {
                if ("左".equals(direction)) {
                    String leftValue = formatNumber(-superElevationValue);
                    finallyData.setLeftEarthShoulder(leftValue);
                    finallyData.setLeftHardShoulder(leftValue);
                    finallyData.setLeftDrivingLane(leftValue);
                    finallyData.setRightDrivingLane(formatNumber(superElevationValue));
                    finallyData.setRightHardShoulder(formatNumber(superElevationValue));
                    finallyData.setRightEarthShoulder(formatNumber(superElevationValue));
                } else {
                    finallyData.setLeftEarthShoulder(formatNumber(superElevationValue));
                    finallyData.setLeftHardShoulder(formatNumber(superElevationValue));
                    finallyData.setLeftDrivingLane(formatNumber(superElevationValue));
                    String rightValue = formatNumber(-superElevationValue);
                    finallyData.setRightDrivingLane(rightValue);
                    finallyData.setRightHardShoulder(rightValue);
                    finallyData.setRightEarthShoulder(rightValue);
                }
            }else{
                finallyData.setLeftEarthShoulder(superelevation);
                finallyData.setLeftHardShoulder(superelevation);
                finallyData.setLeftDrivingLane(superelevation);
                finallyData.setRightDrivingLane(superelevation);
                finallyData.setRightHardShoulder(superelevation);
                finallyData.setRightEarthShoulder(superelevation);
            }
        }

        return finallyData;
    }

    private static String formatNumber(double value) {
        // 将double转换为字符串，并去除尾随的".0"
        String formatted = String.valueOf(value);
        if (formatted.endsWith(".0")) {
            formatted = formatted.substring(0, formatted.length() - 2);
        }
        return formatted;
    }

}
