package com.bulain.mybatis.sys.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 导入错误记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRecord {

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 错误信息
     */
    private String message;

}
