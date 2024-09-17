package com.schdri.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadData {
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

    @ExcelProperty(index = 12)
    private String firstTransitionCurveStart;

    @ExcelProperty(index = 13)
    private String firstTransitionCurveEnd;

    @ExcelProperty(index = 14)
    private String curveMidpoint;

    @ExcelProperty(index = 15)
    private String secondTransitionCurveStart;

    @ExcelProperty(index = 16)
    private String secondTransitionCurveEnd;

    @ExcelProperty(index = 17)
    private String straightLineLength;


}
