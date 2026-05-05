package com.bulain.mybatis.sys.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Excel模板生成服务单元测试
 */
@SpringBootTest
class ExcelTemplateServiceTest {

    @Autowired
    private ExcelTemplateService excelTemplateService;

    @Test
    void testDownloadUserTemplate_ShouldGenerateValidExcel() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        excelTemplateService.downloadUserTemplate(outputStream);

        assertTrue(outputStream.size() > 0, "生成的Excel文件不能为空");
    }

    @Test
    void testDownloadUserTemplate_ShouldHaveTwoSheets() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        excelTemplateService.downloadUserTemplate(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        List<ReadSheet> sheets = EasyExcel.read(inputStream).build().excelExecutor().sheetList();

        assertEquals(2, sheets.size(), "用户模板应包含2个Sheet");
        assertEquals("填写说明", sheets.get(0).getSheetName());
        assertEquals("用户数据", sheets.get(1).getSheetName());
    }

    @Test
    void testDownloadRoleTemplate_ShouldGenerateValidExcel() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        excelTemplateService.downloadRoleTemplate(outputStream);

        assertTrue(outputStream.size() > 0, "生成的Excel文件不能为空");
    }

    @Test
    void testDownloadRoleTemplate_ShouldHaveTwoSheets() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        excelTemplateService.downloadRoleTemplate(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        List<ReadSheet> sheets = EasyExcel.read(inputStream).build().excelExecutor().sheetList();

        assertEquals(2, sheets.size(), "角色模板应包含2个Sheet");
        assertEquals("填写说明", sheets.get(0).getSheetName());
        assertEquals("角色数据", sheets.get(1).getSheetName());
    }

}
