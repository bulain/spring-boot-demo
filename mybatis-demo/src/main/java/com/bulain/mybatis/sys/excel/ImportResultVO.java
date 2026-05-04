package com.bulain.mybatis.sys.excel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 导入结果VO
 */
@Data
public class ImportResultVO {

    /**
     * 成功数量
     */
    private Integer successCount = 0;

    /**
     * 失败数量
     */
    private Integer failCount = 0;

    /**
     * 更新数量
     */
    private Integer updateCount = 0;

    /**
     * 错误列表
     */
    private List<ErrorRecord> errors = new ArrayList<>();

    /**
     * 添加错误
     */
    public void addError(Integer rowNum, String message) {
        errors.add(new ErrorRecord(rowNum, message));
        failCount++;
    }

    /**
     * 成功计数
     */
    public void incrementSuccess() {
        successCount++;
    }

    /**
     * 更新计数
     */
    public void incrementUpdate() {
        updateCount++;
    }

}
