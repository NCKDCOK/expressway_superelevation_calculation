package com.schdri.domain;

import lombok.Data;


@Data
public class FinallyData {

    private String leftEarthShoulder; // 左侧土路肩


    private String leftHardShoulder; // 左侧硬路肩


    private String leftDrivingLane; // 左侧行车道


    private String stationNumber; // 桩号


    private String rightDrivingLane; // 右侧行车道


    private String rightHardShoulder; // 右侧硬路肩

    private String rightEarthShoulder; // 右侧土路肩
}
