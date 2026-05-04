package com.bulain.mybatis.sys.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 角色Excel读取监听器
 */
@Slf4j
public class RoleReadListener implements ReadListener<SysRoleExcel> {

    private static final int BATCH_SIZE = 50;

    private final List<SysRoleExcel> batchList = new ArrayList<>(BATCH_SIZE);
    private final Set<String> processedCodes = new HashSet<>();
    private final ImportResultVO result = new ImportResultVO();
    private int rowNum = 1;

    @Override
    public void invoke(SysRoleExcel data, AnalysisContext context) {
        rowNum++;
        log.debug("读取第{}行数据: {}", rowNum, data);

        // 数据校验
        String errorMsg = validate(data);
        if (errorMsg != null) {
            result.addError(rowNum, errorMsg);
            return;
        }

        // 去重
        if (processedCodes.contains(data.getCode())) {
            result.addError(rowNum, "角色编码重复");
            return;
        }
        processedCodes.add(data.getCode());

        batchList.add(data);

        // 达到批量阈值
        if (batchList.size() >= BATCH_SIZE) {
            log.debug("达到批量处理阈值，当前批次数量：{}", batchList.size());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据读取完成，共{}行，有效{}条", rowNum - 1, batchList.size());
    }

    /**
     * 数据校验
     */
    private String validate(SysRoleExcel data) {
        if (!StringUtils.hasText(data.getCode())) {
            return "角色编码不能为空";
        }
        if (!StringUtils.hasText(data.getName())) {
            return "角色名称不能为空";
        }
        return null;
    }

    public List<SysRoleExcel> getDataList() {
        return batchList;
    }

    public ImportResultVO getResult() {
        return result;
    }

}
