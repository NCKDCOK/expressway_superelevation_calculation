package com.schdri.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class OutPutData {

    private String intersectionNumber;

    private List<FinallyData> finallyDataList;
}
