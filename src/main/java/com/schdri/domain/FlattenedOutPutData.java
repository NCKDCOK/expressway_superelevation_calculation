package com.schdri.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlattenedOutPutData {
    @ExcelProperty(value = "交叉口编号", index = 0)
    private String intersectionNumber;

    @ExcelProperty(value = "左侧土路肩", index = 1)
    private String leftEarthShoulder;

    @ExcelProperty(value = "左侧硬路肩", index = 2)
    private String leftHardShoulder;

    @ExcelProperty(value = "左侧行车道", index = 3)
    private String leftDrivingLane;

    @ExcelProperty(value = "桩号", index = 4)
    private String stationNumber;

    @ExcelProperty(value = "右侧行车道", index = 5)
    private String rightDrivingLane;

    @ExcelProperty(value = "右侧硬路肩", index = 6)
    private String rightHardShoulder;

    @ExcelProperty(value = "右侧土路肩", index = 7)
    private String rightEarthShoulder;




}


