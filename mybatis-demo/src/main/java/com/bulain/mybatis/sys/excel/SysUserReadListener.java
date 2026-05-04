package com.bulain.mybatis.sys.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户Excel读取监听器
 */
@Slf4j
public class SysUserReadListener implements ReadListener<SysUserExcel> {

    private final List<SysUserExcel> dataList = new ArrayList<>();

    @Override
    public void invoke(SysUserExcel data, AnalysisContext context) {
        log.debug("读取到数据: {}", data);
        dataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据读取完成，共{}条", dataList.size());
    }

    public List<SysUserExcel> getDataList() {
        return dataList;
    }

}
