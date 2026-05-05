package com.bulain.mybatis.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 角色Excel导出导入DTO
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(18)
@ColumnWidth(25)
public class SysRoleExcel {

    @ExcelProperty("角色名称")
    private String name;

    @ExcelProperty("角色编码")
    private String code;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("状态")
    private String status;

}
