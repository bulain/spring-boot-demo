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
import java.util.regex.Pattern;

/**
 * 用户Excel流式读取监听器
 * 按批次处理，每批100行立即调用回调处理
 */
@Slf4j
public class UserImportListener implements ReadListener<SysUserExcel> {

    private static final int BATCH_SIZE = 100;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private final List<SysUserExcel> currentBatch = new ArrayList<>(BATCH_SIZE);
    private final Set<String> processedUsernames = new HashSet<>();
    private final ImportResultVO result = new ImportResultVO();
    private final Function<List<SysUserExcel>, ImportResultVO> batchProcessor;
    private int rowNum = 1;

    public UserImportListener(Function<List<SysUserExcel>, ImportResultVO> batchProcessor) {
        this.batchProcessor = batchProcessor;
    }

    @Override
    public void invoke(SysUserExcel data, AnalysisContext context) {
        rowNum++;
        log.debug("读取第{}行数据: {}", rowNum, data);

        // 数据校验
        String errorMsg = validate(data);
        if (errorMsg != null) {
            result.addError(rowNum, errorMsg);
            return;
        }

        // 文件内去重
        if (processedUsernames.contains(data.getUsername())) {
            result.addError(rowNum, "用户名重复");
            return;
        }
        processedUsernames.add(data.getUsername());

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
            // 记录整个批次的错误
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
    private String validate(SysUserExcel data) {
        if (!StringUtils.hasText(data.getUsername())) {
            return "用户名不能为空";
        }
        if (!StringUtils.hasText(data.getName())) {
            return "姓名不能为空";
        }
        if (StringUtils.hasText(data.getPhone()) && !PHONE_PATTERN.matcher(data.getPhone()).matches()) {
            return "手机号格式错误";
        }
        if (StringUtils.hasText(data.getEmail()) && !EMAIL_PATTERN.matcher(data.getEmail()).matches()) {
            return "邮箱格式错误";
        }
        return null;
    }

    public ImportResultVO getResult() {
        return result;
    }

}
