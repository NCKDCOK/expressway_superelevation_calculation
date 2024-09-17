package com.schdri.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.schdri.domain.ReadData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomExcelListener implements ReadListener<ReadData> {
    private List<ReadData> dataList = new ArrayList<>();

    @Override
    public void invoke(ReadData data, AnalysisContext context) {
        int rowIndex = context.readRowHolder().getRowIndex();
        try {
            // 如果数据为null，记录警告日志
            if (data == null) {
                log.warn("行索引: {} 的数据为 null", rowIndex);
            } else {
                dataList.add(data);
                log.info("行索引: {}, 数据: {}", rowIndex, data);  // 日志记录每一行的数据
            }
        } catch (Exception e) {
            log.error("读取行索引: {} 时发生错误: {}", rowIndex, e.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("共读取到 {} 条数据", dataList.size());
    }

    public List<ReadData> getDataList() {
        return dataList;
    }






}