package com.bulain.mybatis.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 用户Excel导出导入DTO
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(18)
@ColumnWidth(25)
public class SysUserExcel {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("状态")
    private String status;

}
