package com.bulain.mybatis.sys.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * 角色Excel流式读取监听器
 * 按批次处理，每批100行立即调用回调处理
 */
@Slf4j
public class RoleImportListener implements ReadListener<SysRoleExcel> {

    private static final int BATCH_SIZE = 100;

    private final List<SysRoleExcel> currentBatch = new ArrayList<>(BATCH_SIZE);
    private final Set<String> processedCodes = new HashSet<>();
    private final ImportResultVO result = new ImportResultVO();
    private final Function<List<SysRoleExcel>, ImportResultVO> batchProcessor;
    private int rowNum = 1;

    public RoleImportListener(Function<List<SysRoleExcel>, ImportResultVO> batchProcessor) {
        this.batchProcessor = batchProcessor;
    }

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

        // 文件内去重
        if (processedCodes.contains(data.getCode())) {
            result.addError(rowNum, "角色编码重复");
            return;
        }
        processedCodes.add(data.getCode());

        currentBatch.add(data);

        // 达到批量阈值，立即处理
        if (currentBatch.size() >= BATCH_SIZE) {
            processBatch();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 处理剩余的部分批次
        if (!currentBatch.isEmpty()) {
            processBatch();
        }
        log.info("所有数据读取完成，共{}行，成功{}条，失败{}条，更新{}条",
                rowNum - 1, result.getSuccessCount(), result.getFailCount(), result.getUpdateCount());
    }

    /**
     * 处理当前批次，调用回调，清空批次
     */
    private void processBatch() {
        if (currentBatch.isEmpty()) {
            return;
        }

        log.debug("处理批次，大小：{}", currentBatch.size());
        try {
            ImportResultVO batchResult = batchProcessor.apply(currentBatch);
            // 聚合批次结果到总结果
            result.setSuccessCount(result.getSuccessCount() + batchResult.getSuccessCount());
            result.setUpdateCount(result.getUpdateCount() + batchResult.getUpdateCount());
            result.getErrors().addAll(batchResult.getErrors());
            result.setFailCount(result.getFailCount() + batchResult.getFailCount());
        } catch (Exception e) {
            log.error("批次处理失败", e);
            // 记录整个批次的错误（从 rowNum - batchSize 到 rowNum）
            int startRow = rowNum - currentBatch.size() + 1;
            for (int i = 0; i < currentBatch.size(); i++) {
                result.addError(startRow + i, "批次处理失败: " + e.getMessage());
            }
        } finally {
            currentBatch.clear(); // 释放内存
        }
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

    public ImportResultVO getResult() {
        return result;
    }

}
