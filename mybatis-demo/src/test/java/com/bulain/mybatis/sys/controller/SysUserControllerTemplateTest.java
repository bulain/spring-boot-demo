package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.sys.service.SysJwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户模板下载接口集成测试
 */
@SpringBootTest
class SysUserControllerTemplateTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SysJwtService sysJwtService;

    private MockMvc mockMvc;
    private String adminToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        // 生成管理员Token
        adminToken = sysJwtService.generateToken("test-admin-id", "admin");
    }

    @Test
    void testDownloadUserTemplate_ShouldReturnExcelFile() throws Exception {
        mockMvc.perform(get("/api/sys/users/template")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", org.hamcrest.Matchers.containsString("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")))
                .andExpect(header().string("Content-disposition", org.hamcrest.Matchers.containsString("%E7%94%A8%E6%88%B7%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF")))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsByteArray().length > 0));
    }

    @Test
    void testDownloadUserTemplate_ShouldEncodeChineseFileName() throws Exception {
        mockMvc.perform(get("/api/sys/users/template")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-disposition", org.hamcrest.Matchers.containsString("utf-8")));
    }

}
