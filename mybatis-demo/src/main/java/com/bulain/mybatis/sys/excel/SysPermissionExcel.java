package com.bulain.mybatis.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 权限Excel导出导入DTO
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(18)
@ColumnWidth(25)
public class SysPermissionExcel {

    @ExcelProperty("权限名称")
    private String name;

    @ExcelProperty("权限编码")
    private String code;

    @ExcelProperty("资源类型")
    private String resourceType;

    @ExcelProperty("描述")
    private String description;

}
