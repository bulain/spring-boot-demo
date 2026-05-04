package com.bulain.mybatis.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 用户角色分配Excel导出导入DTO
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(18)
@ColumnWidth(25)
public class SysUserRoleExcel {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("用户姓名")
    private String userName;

    @ExcelProperty("角色编码")
    private String roleCode;

    @ExcelProperty("角色名称")
    private String roleName;

}
