package com.bulain.mybatis.sys.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.bulain.mybatis.sys.excel.SysRoleExcel;
import com.bulain.mybatis.sys.excel.SysUserExcel;
import com.bulain.mybatis.sys.service.ExcelTemplateService;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel模板生成服务实现类
 */
@Service
public class ExcelTemplateServiceImpl implements ExcelTemplateService {

    @Override
    public void downloadUserTemplate(OutputStream outputStream) {
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream).build()) {
            // Sheet 1: 填写说明
            WriteSheet instructionSheet = EasyExcel.writerSheet(0, "填写说明")
                    .head(InstructionRow.class)
                    .build();
            excelWriter.write(getUserInstructions(), instructionSheet);

            // Sheet 2: 用户数据
            WriteSheet dataSheet = EasyExcel.writerSheet(1, "用户数据")
                    .head(SysUserExcel.class)
                    .build();
            excelWriter.write(getUserSampleData(), dataSheet);
        }
    }

    @Override
    public void downloadRoleTemplate(OutputStream outputStream) {
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream).build()) {
            // Sheet 1: 填写说明
            WriteSheet instructionSheet = EasyExcel.writerSheet(0, "填写说明")
                    .head(InstructionRow.class)
                    .build();
            excelWriter.write(getRoleInstructions(), instructionSheet);

            // Sheet 2: 角色数据
            WriteSheet dataSheet = EasyExcel.writerSheet(1, "角色数据")
                    .head(SysRoleExcel.class)
                    .build();
            excelWriter.write(getRoleSampleData(), dataSheet);
        }
    }

    private List<InstructionRow> getUserInstructions() {
        List<InstructionRow> instructions = new ArrayList<>();
        instructions.add(new InstructionRow("字段名称", "是否必填", "字段说明", "示例值"));
        instructions.add(new InstructionRow("用户名", "是", "系统唯一标识，3-50个字符，字母数字组合", "admin"));
        instructions.add(new InstructionRow("姓名", "是", "用户真实姓名，2-50个字符", "张三"));
        instructions.add(new InstructionRow("邮箱", "否", "有效的邮箱地址格式", "zhangsan@example.com"));
        instructions.add(new InstructionRow("手机号", "否", "11位中国大陆手机号格式", "13800138000"));
        instructions.add(new InstructionRow("密码", "是", "明文填写，导入时系统自动加密", "123456"));
        instructions.add(new InstructionRow("状态", "是", "只能填写 \"启用\" 或 \"禁用\"", "启用"));
        return instructions;
    }

    private List<InstructionRow> getRoleInstructions() {
        List<InstructionRow> instructions = new ArrayList<>();
        instructions.add(new InstructionRow("字段名称", "是否必填", "字段说明", "示例值"));
        instructions.add(new InstructionRow("角色编码", "是", "系统唯一标识，3-100个字符，字母下划线", "ROLE_ADMIN"));
        instructions.add(new InstructionRow("角色名称", "是", "角色显示名称，2-50个字符", "系统管理员"));
        instructions.add(new InstructionRow("描述", "否", "角色功能描述", "管理系统所有功能"));
        instructions.add(new InstructionRow("状态", "是", "只能填写 \"启用\" 或 \"禁用\"", "启用"));
        return instructions;
    }

    private List<SysUserExcel> getUserSampleData() {
        List<SysUserExcel> sampleData = new ArrayList<>();
        SysUserExcel sample = new SysUserExcel();
        sample.setUsername("zhangsan");
        sample.setName("张三");
        sample.setEmail("zhangsan@example.com");
        sample.setPhone("13800138000");
        sample.setPassword("123456");
        sample.setStatus("启用");
        sampleData.add(sample);
        return sampleData;
    }

    private List<SysRoleExcel> getRoleSampleData() {
        List<SysRoleExcel> sampleData = new ArrayList<>();
        SysRoleExcel sample = new SysRoleExcel();
        sample.setCode("ROLE_ADMIN");
        sample.setName("系统管理员");
        sample.setDescription("管理系统所有功能");
        sample.setStatus("启用");
        sampleData.add(sample);
        return sampleData;
    }

    /**
     * 填写说明Sheet的行结构
     */
    @lombok.Data
    public static class InstructionRow {
        @com.alibaba.excel.annotation.ExcelProperty("字段名称")
        private String fieldName;
        @com.alibaba.excel.annotation.ExcelProperty("是否必填")
        private String required;
        @com.alibaba.excel.annotation.ExcelProperty("字段说明")
        private String description;
        @com.alibaba.excel.annotation.ExcelProperty("示例值")
        private String sample;

        public InstructionRow() {
        }

        public InstructionRow(String fieldName, String required, String description, String sample) {
            this.fieldName = fieldName;
            this.required = required;
            this.description = description;
            this.sample = sample;
        }
    }

}
