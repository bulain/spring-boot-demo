package com.bulain.mybatis.sys.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.UpdateUserDTO;
import com.bulain.mybatis.sys.dto.UserQueryDTO;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.excel.ImportResultVO;
import com.bulain.mybatis.sys.excel.SysUserExcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class SysUserServiceTest {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @BeforeEach
    void setUp() {
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testCreateUser() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("serviceuser");
        dto.setName("Service User");
        dto.setEmail("service@example.com");
        dto.setPhone("13900139000");
        dto.setPassword("password123!");

        SysUser user = sysUserService.createUser(dto);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("serviceuser", user.getUsername());
    }

    @Test
    void testUpdateUser() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("updateuser");
        createDTO.setName("Update User");
        createDTO.setPassword("password123!");
        SysUser user = sysUserService.createUser(createDTO);

        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@example.com");

        SysUser updated = sysUserService.updateUser(user.getId(), updateDTO);

        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@example.com", updated.getEmail());
    }

    @Test
    void testToggleStatus() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("statususer");
        createDTO.setName("Status User");
        createDTO.setPassword("password123!");
        SysUser user = sysUserService.createUser(createDTO);

        sysUserService.toggleStatus(user.getId(), 0);

        SysUser disabled = sysUserService.getById(user.getId());
        assertEquals(0, disabled.getStatus());
    }

    @Test
    void testPageUsers() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("pageuser");
        createDTO.setName("Page User");
        createDTO.setPassword("password123!");
        sysUserService.createUser(createDTO);

        UserQueryDTO query = new UserQueryDTO();
        query.setName("Page");
        query.setCurrent(1);
        query.setSize(10);

        Paged<SysUser> paged = sysUserService.pageUsers(query);

        assertNotNull(paged);
        assertTrue(paged.getTotalCount() > 0);
    }

    @Test
    void testGetByUsername() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("getbyusername");
        createDTO.setName("Get By Username");
        createDTO.setPassword("password123!");
        sysUserService.createUser(createDTO);

        SysUser found = sysUserService.getByUsername("getbyusername");

        assertNotNull(found);
        assertEquals("Get By Username", found.getName());
    }

    @Test
    void testAssignRoles() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("assignroleuser");
        createDTO.setName("Assign Role User");
        createDTO.setPassword("password123!");
        SysUser user = sysUserService.createUser(createDTO);

        // Note: In a real test, we'd create roles first then assign them
        // This is just testing the method call structure
        sysUserService.assignRoles(user.getId(), List.of());

        // Verify no exception thrown
        assertTrue(true);
    }

    @Test
    void testGetUserPermissionCodes() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("permuser");
        createDTO.setName("Permission User");
        createDTO.setPassword("password123!");
        SysUser user = sysUserService.createUser(createDTO);

        Set<String> permissions = sysUserService.getUserPermissionCodes(user.getId());

        assertNotNull(permissions);
        // User without roles should have empty permissions
        assertTrue(permissions.isEmpty());
    }

    @Test
    void testImportUsers() throws Exception {
        // 准备测试数据
        List<SysUserExcel> dataList = List.of(
            createUserExcel("importuser001", "Import User 001", "import001@example.com", "13900139001"),
            createUserExcel("importuser002", "Import User 002", "import002@example.com", "13900139002")
        );

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysUserExcel.class).sheet().doWrite(dataList);

        // 执行流式导入
        ImportResultVO result = sysUserService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getSuccessCount());
        assertEquals(0, result.getUpdateCount());
        assertEquals(0, result.getFailCount());

        // 验证数据已创建
        SysUser user1 = sysUserService.getByUsername("importuser001");
        SysUser user2 = sysUserService.getByUsername("importuser002");
        assertNotNull(user1);
        assertNotNull(user2);
    }

    @Test
    void testImportUsersWithUpdate() throws Exception {
        // 先创建一个已存在的用户
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("updateimport");
        dto.setName("Original Name");
        dto.setPassword("password123!");
        sysUserService.createUser(dto);

        // 准备导入数据（包含已存在的，会更新）
        List<SysUserExcel> dataList = List.of(
            createUserExcel("updateimport", "Updated Name", "updated@example.com", "13800138000"),
            createUserExcel("newimport", "New User", "new@example.com", "13800138001")
        );

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysUserExcel.class).sheet().doWrite(dataList);

        // 执行流式导入
        ImportResultVO result = sysUserService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getSuccessCount());
        assertEquals(1, result.getUpdateCount());
        assertEquals(0, result.getFailCount());

        // 验证更新
        SysUser updated = sysUserService.getByUsername("updateimport");
        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@example.com", updated.getEmail());
    }

    @Test
    void testUserImportListenerValidation() throws Exception {
        // 准备测试数据（包含空值和格式错误）
        List<SysUserExcel> dataList = List.of(
            createUserExcel(null, "Valid Name", "email@example.com", "13900139000"), // 用户名为空
            createUserExcel("VALID_USER", null, "email@example.com", "13900139001"), // 姓名为空
            createUserExcel("VALID_USER2", "Valid Name 2", "invalid-email", "13900139002"), // 邮箱格式错误
            createUserExcel("VALID_USER3", "Valid Name 3", "valid@example.com", "123"), // 手机号格式错误
            createUserExcel("VALID_USER4", "Valid Name 4", "valid2@example.com", "13900139004") // 有效数据
        );

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysUserExcel.class).sheet().doWrite(dataList);

        // 流式导入（包含校验）
        ImportResultVO result = sysUserService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 第1、2、3、4行应该校验失败
        assertEquals(4, result.getFailCount());
        // 第5行成功
        assertEquals(1, result.getSuccessCount());
    }

    @Test
    void testStreamingBatchProcessing() throws Exception {
        // 创建 101 行数据（超过一批，验证分批处理）
        List<SysUserExcel> dataList = new ArrayList<>();
        for (int i = 1; i <= 101; i++) {
            SysUserExcel excel = new SysUserExcel();
            excel.setUsername("BATCH_USER_" + i);
            excel.setName("Batch User " + i);
            excel.setEmail("batch" + i + "@example.com");
            excel.setPhone("139" + String.format("%08d", i));
            excel.setStatus("启用");
            dataList.add(excel);
        }

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysUserExcel.class).sheet().doWrite(dataList);

        // 执行流式导入
        ImportResultVO result = sysUserService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 验证101行全部成功导入
        assertEquals(101, result.getSuccessCount());
        assertEquals(0, result.getUpdateCount());
        assertEquals(0, result.getFailCount());
    }

    @Test
    void testEmptyFileImport() throws Exception {
        // 空数据（只有表头，无数据行）
        List<SysUserExcel> dataList = List.of();

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysUserExcel.class).sheet().doWrite(dataList);

        // 流式导入
        ImportResultVO result = sysUserService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 验证空文件不报错
        assertNotNull(result);
        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getUpdateCount());
        assertEquals(0, result.getFailCount());
    }

    @Test
    void testDuplicateUsernameInFile() throws Exception {
        // 准备测试数据（包含重复用户名）
        List<SysUserExcel> dataList = List.of(
            createUserExcel("dupuser", "First User", "first@example.com", "13900139000"),
            createUserExcel("dupuser", "Duplicate User", "dup@example.com", "13900139001") // 重复
        );

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysUserExcel.class).sheet().doWrite(dataList);

        // 流式导入
        ImportResultVO result = sysUserService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 第1行成功，第2行应该校验失败（重复）
        assertEquals(1, result.getSuccessCount());
        assertEquals(1, result.getFailCount());
    }

    @Test
    void testExportUsers() throws Exception {
        // 创建测试数据
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("exportuser001");
        dto.setName("Export Test User 001");
        dto.setEmail("export001@example.com");
        dto.setPhone("13900139001");
        dto.setPassword("password123!");
        sysUserService.createUser(dto);

        CreateUserDTO dto2 = new CreateUserDTO();
        dto2.setUsername("exportuser002");
        dto2.setName("Export Test User 002");
        dto2.setEmail("export002@example.com");
        dto2.setPhone("13900139002");
        dto2.setPassword("password123!");
        sysUserService.createUser(dto2);

        // 测试导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserQueryDTO query = new UserQueryDTO();
        sysUserService.export(query, response);

        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType().startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    @Test
    void testExportUsersByIds() throws Exception {
        // 创建测试数据
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("exportid001");
        dto.setName("Export ID Test User 001");
        dto.setEmail("eid001@example.com");
        dto.setPhone("13900139101");
        dto.setPassword("password123!");
        SysUser user1 = sysUserService.createUser(dto);

        CreateUserDTO dto2 = new CreateUserDTO();
        dto2.setUsername("exportid002");
        dto2.setName("Export ID Test User 002");
        dto2.setEmail("eid002@example.com");
        dto2.setPhone("13900139102");
        dto2.setPassword("password123!");
        sysUserService.createUser(dto2);

        // 测试按ID导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        sysUserService.exportByIds(List.of(user1.getId()), response);

        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testExportUsersByIdsEmptyList() {
        // 测试空ID列表导出（应抛出异常）
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertThrows(RuntimeException.class, () -> {
            sysUserService.exportByIds(List.of(), response);
        });
    }

    @Test
    void testStreamingExportEmptyData() throws Exception {
        // 测试空数据导出（0行）
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserQueryDTO query = new UserQueryDTO();
        sysUserService.export(query, response);

        // 验证生成了Excel文件（至少有表头）
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportSmallBatch() throws Exception {
        // 创建 99 行数据（不满一批）
        for (int i = 1; i <= 99; i++) {
            CreateUserDTO dto = new CreateUserDTO();
            dto.setUsername("smalluser" + i);
            dto.setName("Small User " + i);
            dto.setEmail("small" + i + "@example.com");
            dto.setPhone("139" + String.format("%08d", i));
            dto.setPassword("password123!");
            sysUserService.createUser(dto);
        }

        // 测试导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserQueryDTO query = new UserQueryDTO();
        sysUserService.export(query, response);

        // 验证导出成功
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportMultiBatch() throws Exception {
        // 创建 101 行数据（两批次）
        for (int i = 1; i <= 101; i++) {
            CreateUserDTO dto = new CreateUserDTO();
            dto.setUsername("multiuser" + i);
            dto.setName("Multi User " + i);
            dto.setEmail("multi" + i + "@example.com");
            dto.setPhone("138" + String.format("%08d", i));
            dto.setPassword("password123!");
            sysUserService.createUser(dto);
        }

        // 测试导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserQueryDTO query = new UserQueryDTO();
        sysUserService.export(query, response);

        // 验证导出成功
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportWithFilter() throws Exception {
        // 创建测试数据
        CreateUserDTO dto1 = new CreateUserDTO();
        dto1.setUsername("filteruser001");
        dto1.setName("Filter Test User 001");
        dto1.setEmail("filter001@example.com");
        dto1.setPhone("13900139201");
        dto1.setPassword("password123!");
        sysUserService.createUser(dto1);

        CreateUserDTO dto2 = new CreateUserDTO();
        dto2.setUsername("filteruser002");
        dto2.setName("Filter Test User 002");
        dto2.setEmail("filter002@example.com");
        dto2.setPhone("13900139202");
        dto2.setPassword("password123!");
        sysUserService.createUser(dto2);

        // 按名称筛选导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserQueryDTO query = new UserQueryDTO();
        query.setName("001");
        sysUserService.export(query, response);

        // 验证导出成功
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    private SysUserExcel createUserExcel(String username, String name, String email, String phone) {
        SysUserExcel excel = new SysUserExcel();
        excel.setUsername(username);
        excel.setName(name);
        excel.setEmail(email);
        excel.setPhone(phone);
        excel.setStatus("启用");
        return excel;
    }
}
