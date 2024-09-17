package com.schdri.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WriteData {

        @ExcelProperty(index = 0)
        private String intersectionNumber;

        @ExcelProperty(index = 1)
        private String northCoordinate;

        @ExcelProperty(index = 2)
        private String eastCoordinate;

        @ExcelProperty(index = 3)
        private String stakeNumber;

        @ExcelProperty(index = 4)
        private String deflectionAngle;

        @ExcelProperty(index = 5)
        private String radius;

        @ExcelProperty(index = 6)
        private String firstTransitionCurveStart;

        @ExcelProperty(index = 7)
        private String firstTransitionCurveEnd;

        @ExcelProperty(index = 8)
        private String curveMidpoint;

        @ExcelProperty(index = 9)
        private String secondTransitionCurveStart;

        @ExcelProperty(index = 10)
        private String secondTransitionCurveEnd;

        @ExcelProperty(index = 11)
        private String firstStraightLength;

        @ExcelProperty(index = 12)
        private String firstTransitionCurveLength;

        @ExcelProperty(index = 13)
        private String curveLength;

        @ExcelProperty(index = 14)
        private String secondTransitionCurveLength;

        @ExcelProperty(index = 15)
        private String secondStraightLength;

        @ExcelProperty(index = 16)
        private String direction;

        @ExcelProperty(index = 17)
        private String first;

        @ExcelProperty(index = 18)
        private String second;

        @ExcelProperty(index = 19)
        private String curveType;

        @ExcelProperty(index = 20)
        private String superelevation;

        @ExcelProperty(index = 21)
        // 前Δi（偏角）
        private String precedingDeflectionAngle;

        @ExcelProperty(index = 22)
        // 前最小渐变段长度
        private String minPrecedingTransitionLength;

        @ExcelProperty(index = 23)
        // 前最大渐变段长度
        private String maxPrecedingTransitionLength;

        @ExcelProperty(index = 24)
        // 后最小渐变段长度
        private String minSucceedingTransitionLength;

        @ExcelProperty(index = 25)
        // 后最大渐变段长度
        private String maxSucceedingTransitionLength;



        @ExcelProperty(index = 26)
        // 后Δi（偏角）
        private String succeedingDeflectionAngle;

        @ExcelProperty(index = 27)
        //选定前渐变长度
        private String selectedPrecedingTransitionLength;

        @ExcelProperty(index = 28)
        //选定后渐变长度
        private String selectedSucceedingTransitionLength;

        @ExcelProperty(index = 29)
        private String precedingTransitionStartPoint; // 前渐变段起点

        @ExcelProperty(index = 30)
        private String precedingTransitionStartPointSuperelevation; // 前渐变段起点超高值

        @ExcelProperty(index = 31)
        private String insidePrecedingTransitionStartPoint; // 前渐变段内侧起点

        @ExcelProperty(index = 32)
        private String nodeSettingPreceding; // 节点设置

        @ExcelProperty(index = 33)
        private String precedingTransitionEndPoint; // 前渐变段终点

        @ExcelProperty(index = 34)
        private String precedingTransitionEndPointSuperelevation; // 前渐变段终点超高值

        @ExcelProperty(index = 35)
        private String succeedingTransitionStartPoint; // 后渐变段起点

        @ExcelProperty(index = 36)
        private String succeedingTransitionStartPointSuperelevation; // 后渐变段起点超高值

        @ExcelProperty(index = 37)
        private String insideSucceedingTransitionEndPoint; // 后渐变段内侧终点

        @ExcelProperty(index = 38)
        private String nodeSettingSucceeding; // 节点设置

        @ExcelProperty(index = 39)
        private String succeedingTransitionEndPoint; // 后渐变段终点

        @ExcelProperty(index = 40)
        private String succeedingTransitionEndPointSuperelevation; // 后渐变段终点超高值

        @ExcelProperty(index = 41)
        private String leftEarthShoulder; // 左侧土路肩

        @ExcelProperty(index = 42)
        private String leftHardShoulder; // 左侧硬路肩

        @ExcelProperty(index = 43)
        private String leftDrivingLane; // 左侧行车道

        @ExcelProperty(index = 44)
        private String stationNumber; // 桩号

        @ExcelProperty(index = 45)
        private String rightDrivingLane; // 右侧行车道

        @ExcelProperty(index = 46)
        private String rightHardShoulder; // 右侧硬路肩

}
