package com.schdri.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.schdri.domain.*;
import com.schdri.listener.CustomExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class ExcelUtil {



    public static List<ReadData> readExcel(String filePath) throws Exception {
        CustomExcelListener listener = new CustomExcelListener();

        FileInputStream inputStream = new FileInputStream(filePath);

        ExcelTypeEnum excelType = filePath.toLowerCase().endsWith(".xlsx") ?
                ExcelTypeEnum.XLSX : ExcelTypeEnum.XLS;

        EasyExcel.read(inputStream, ReadData.class, listener)
                .excelType(excelType)
                .sheet()
                .headRowNumber(6) // 设置标题行数为6，从第7行开始读取数据
                .doRead();

        inputStream.close();

        return listener.getDataList();
    }


    public static void writeExcel(String filePath, List<WriteData> data) throws Exception {
        // 自定义表头
        List<List<String>> head = new ArrayList<>();
        head.add(new ArrayList<>(Arrays.asList("交点号")));
        head.add(new ArrayList<>(Arrays.asList("交点坐标", "N (X)")));
        head.add(new ArrayList<>(Arrays.asList("交点坐标", "E (Y)")));
        head.add(new ArrayList<>(Arrays.asList("交点桩号")));
        head.add(new ArrayList<>(Arrays.asList("转角值")));
        head.add(new ArrayList<>(Arrays.asList("曲线半径")));
        head.add(new ArrayList<>(Arrays.asList("曲线主点桩号", "第一缓和曲线起点")));
        head.add(new ArrayList<>(Arrays.asList("曲线主点桩号", "第一缓和曲线终点或圆曲线起点")));
        head.add(new ArrayList<>(Arrays.asList("曲线主点桩号", "曲线中点")));
        head.add(new ArrayList<>(Arrays.asList("曲线主点桩号", "第二缓和曲线起点或圆曲线终点")));
        head.add(new ArrayList<>(Arrays.asList("曲线主点桩号", "第二缓和曲线终点")));
        head.add(new ArrayList<>(Arrays.asList("第一直线长度")));
        head.add(new ArrayList<>(Arrays.asList("第一缓和曲线长度")));
        head.add(new ArrayList<>(Arrays.asList("曲线长度")));
        head.add(new ArrayList<>(Arrays.asList("第二缓和曲线长度")));
        head.add(new ArrayList<>(Arrays.asList("第二直线长度")));
        head.add(new ArrayList<>(Arrays.asList("转向")));
        head.add(new ArrayList<>(Arrays.asList("第一次判断")));
        head.add(new ArrayList<>(Arrays.asList("第二次判断")));
        head.add(new ArrayList<>(Arrays.asList("曲线类型")));
        head.add(new ArrayList<>(Arrays.asList("超高")));
        head.add(new ArrayList<>(Arrays.asList("前Δi（偏角）")));
        head.add(new ArrayList<>(Arrays.asList("前最小渐变段长度")));
        head.add(new ArrayList<>(Arrays.asList("前最大渐变段长度")));
        head.add(new ArrayList<>(Arrays.asList("后最小渐变段长度")));
        head.add(new ArrayList<>(Arrays.asList("后最大渐变段长度")));
        head.add(new ArrayList<>(Arrays.asList("后Δi（偏角）")));
        head.add(new ArrayList<>(Arrays.asList("选定前渐变长度")));
        head.add(new ArrayList<>(Arrays.asList("选定后渐变长度")));
        head.add(new ArrayList<>(Arrays.asList("前渐变段起点")));
        head.add(new ArrayList<>(Arrays.asList("前渐变段起点超高值")));
        head.add(new ArrayList<>(Arrays.asList("前渐变段内侧起点")));
        head.add(new ArrayList<>(Arrays.asList("节点设置")));
        head.add(new ArrayList<>(Arrays.asList("前渐变段终点")));
        head.add(new ArrayList<>(Arrays.asList("前渐变段终点超高值")));
        head.add(new ArrayList<>(Arrays.asList("后渐变段起点")));
        head.add(new ArrayList<>(Arrays.asList("后渐变段起点超高值")));
        head.add(new ArrayList<>(Arrays.asList("后渐变段内侧终点")));
        head.add(new ArrayList<>(Arrays.asList("节点设置")));
        head.add(new ArrayList<>(Arrays.asList("后渐变段终点")));
        head.add(new ArrayList<>(Arrays.asList("后渐变段终点超高值")));
        head.add(new ArrayList<>(Arrays.asList("前最小渐变段长度")));
        head.add(new ArrayList<>(Arrays.asList("前最大渐变段长度")));
        head.add(new ArrayList<>(Arrays.asList("前Δi（偏角）")));
        head.add(new ArrayList<>(Arrays.asList("后最小渐变段长度")));
        head.add(new ArrayList<>(Arrays.asList("后最大渐变段长度")));
        head.add(new ArrayList<>(Arrays.asList("后Δi（偏角）")));
        head.add(new ArrayList<>(Arrays.asList("选定前渐变长度")));
        head.add(new ArrayList<>(Arrays.asList("选定后渐变长度")));

        // 设置单元格样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, new WriteCellStyle());

        // 写入Excel
        ExcelWriter excelWriter = EasyExcel.write(filePath).head(head).registerWriteHandler(horizontalCellStyleStrategy).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();
        excelWriter.write(data, writeSheet);
        excelWriter.finish();
        log.info("Excel文件写入成功: {}", filePath);
        log.info("共写入 {} 条数据", data.size());
    }


    public static void output(String filePath, List<OutPutData> dataList) {
        List<FlattenedOutPutData> flattenedDataList = flattenOutPutData(dataList);

        // 设置单元格样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, new WriteCellStyle());

        // 创建自定义的单元格合并策略
        CustomMergeStrategy customMergeStrategy = new CustomMergeStrategy(6, 0);

        // 写入Excel
        EasyExcel.write(filePath, FlattenedOutPutData.class)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .registerWriteHandler(customMergeStrategy)
                .sheet("Sheet1")
                .doWrite(flattenedDataList);

        log.info("Excel文件写入成功: {}", filePath);
        log.info("共写入 {} 条数据", flattenedDataList.size());
    }

    private static class CustomMergeStrategy implements CellWriteHandler {
        private final int mergeRows;
        private final int mergeColumn;

        public CustomMergeStrategy(int mergeRows, int mergeColumn) {
            this.mergeRows = mergeRows;
            this.mergeColumn = mergeColumn;
        }

        @Override
        public void afterCellDispose(CellWriteHandlerContext context) {
            Sheet sheet = context.getWriteSheetHolder().getSheet();
            int rowIndex = context.getRowIndex();
            int colIndex = context.getColumnIndex();

            if (colIndex == mergeColumn && rowIndex % mergeRows == 0 && rowIndex > 0) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowIndex - mergeRows + 1, rowIndex, colIndex, colIndex);
                sheet.addMergedRegionUnsafe(cellRangeAddress);
            }
        }
    }

    public static List<FlattenedOutPutData> flattenOutPutData(List<OutPutData> outPutDataList) {
        List<FlattenedOutPutData> flattenedList = new ArrayList<>();

        for (OutPutData outPutData : outPutDataList) {
            String intersectionNumber = outPutData.getIntersectionNumber();
            List<FinallyData> finallyDataList = outPutData.getFinallyDataList();

            for (int i = 0; i < finallyDataList.size(); i++) {
                FinallyData finallyData = finallyDataList.get(i);
                FlattenedOutPutData flattenedData = new FlattenedOutPutData(
                        i % 6 == 0 ? intersectionNumber : null,
                        finallyData.getLeftEarthShoulder(),
                        finallyData.getLeftHardShoulder(),
                        finallyData.getLeftDrivingLane(),
                        finallyData.getStationNumber(),
                        finallyData.getRightDrivingLane(),
                        finallyData.getRightHardShoulder(),
                        finallyData.getRightEarthShoulder()
                );

                flattenedList.add(flattenedData);
            }
        }

        return flattenedList;
    }



}
