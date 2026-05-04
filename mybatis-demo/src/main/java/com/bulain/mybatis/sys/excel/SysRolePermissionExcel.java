package com.bulain.mybatis.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 角色权限分配Excel导出导入DTO
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(18)
@ColumnWidth(25)
public class SysRolePermissionExcel {

    @ExcelProperty("角色编码")
    private String roleCode;

    @ExcelProperty("角色名称")
    private String roleName;

    @ExcelProperty("权限编码")
    private String permissionCode;

    @ExcelProperty("权限名称")
    private String permissionName;

    @ExcelProperty("资源类型")
    private String resourceType;

}
