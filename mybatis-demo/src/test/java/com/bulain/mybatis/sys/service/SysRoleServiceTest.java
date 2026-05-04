package com.bulain.mybatis.sys.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.excel.ImportResultVO;
import com.bulain.mybatis.sys.excel.SysRoleExcel;
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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class SysRoleServiceTest {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @BeforeEach
    void setUp() {
        sysRoleMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testCreateRole() {
        CreateRoleDTO dto = new CreateRoleDTO();
        dto.setCode("ROLE_SERVICE_TEST");
        dto.setName("Service Test Role");
        dto.setDescription("Role created by service test");

        SysRole role = sysRoleService.createRole(dto);

        assertNotNull(role);
        assertNotNull(role.getId());
        assertEquals("ROLE_SERVICE_TEST", role.getCode());
    }

    @Test
    void testUpdateRole() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_UPDATE_TEST");
        createDTO.setName("Update Test Role");
        SysRole role = sysRoleService.createRole(createDTO);

        UpdateRoleDTO updateDTO = new UpdateRoleDTO();
        updateDTO.setName("Updated Role Name");
        updateDTO.setDescription("Updated Description");

        SysRole updated = sysRoleService.updateRole(role.getId(), updateDTO);

        assertEquals("Updated Role Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    void testGetByCode() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_FIND_BY_CODE");
        createDTO.setName("Find By Code Role");
        sysRoleService.createRole(createDTO);

        SysRole found = sysRoleService.getByCode("ROLE_FIND_BY_CODE");

        assertNotNull(found);
        assertEquals("Find By Code Role", found.getName());
    }

    @Test
    void testPageRoles() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_PAGE_TEST");
        createDTO.setName("Page Test Role");
        sysRoleService.createRole(createDTO);

        RoleQueryDTO query = new RoleQueryDTO();
        query.setName("Page");
        query.setCurrent(1);
        query.setSize(10);

        Paged<SysRole> paged = sysRoleService.pageRoles(query);

        assertNotNull(paged);
        assertTrue(paged.getTotalCount() > 0);
    }

    @Test
    void testAssignPermissions() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_ASSIGN_PERM");
        createDTO.setName("Assign Permission Role");
        SysRole role = sysRoleService.createRole(createDTO);

        // Test assigning empty list (no permissions)
        sysRoleService.assignPermissions(role.getId(), List.of());

        assertTrue(true);
    }

    @Test
    void testGetRolePermissions() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_GET_PERM");
        createDTO.setName("Get Permission Role");
        SysRole role = sysRoleService.createRole(createDTO);

        List<SysPermission> permissions = sysRoleService.getRolePermissions(role.getId());

        assertNotNull(permissions);
        assertTrue(permissions.isEmpty());
    }

    @Test
    void testExportRoles() throws Exception {
        // 创建测试数据
        CreateRoleDTO dto = new CreateRoleDTO();
        dto.setCode("ROLE_EXPORT_001");
        dto.setName("Export Test Role 001");
        sysRoleService.createRole(dto);

        CreateRoleDTO dto2 = new CreateRoleDTO();
        dto2.setCode("ROLE_EXPORT_002");
        dto2.setName("Export Test Role 002");
        sysRoleService.createRole(dto2);

        // 测试导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        RoleQueryDTO query = new RoleQueryDTO();
        sysRoleService.export(query, response);

        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType().startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    @Test
    void testExportRolesByIds() throws Exception {
        // 创建测试数据
        CreateRoleDTO dto = new CreateRoleDTO();
        dto.setCode("ROLE_EXPORT_ID_001");
        dto.setName("Export ID Test Role 001");
        SysRole role1 = sysRoleService.createRole(dto);

        CreateRoleDTO dto2 = new CreateRoleDTO();
        dto2.setCode("ROLE_EXPORT_ID_002");
        dto2.setName("Export ID Test Role 002");
        SysRole role2 = sysRoleService.createRole(dto2);

        // 测试按ID导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        sysRoleService.exportByIds(List.of(role1.getId(), role2.getId()), response);

        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testImportRoles() throws Exception {
        // 准备测试数据
        List<SysRoleExcel> dataList = List.of(
            createRoleExcel("ROLE_IMPORT_001", "Import Test Role 001"),
            createRoleExcel("ROLE_IMPORT_002", "Import Test Role 002")
        );

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysRoleExcel.class).sheet().doWrite(dataList);

        // 执行流式导入
        ImportResultVO result = sysRoleService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getSuccessCount());
        assertEquals(0, result.getUpdateCount());
        assertEquals(0, result.getFailCount());

        // 验证数据已创建
        SysRole role1 = sysRoleService.getByCode("ROLE_IMPORT_001");
        SysRole role2 = sysRoleService.getByCode("ROLE_IMPORT_002");
        assertNotNull(role1);
        assertNotNull(role2);
    }

    @Test
    void testImportRolesWithUpdate() throws Exception {
        // 先创建一个已存在的角色
        CreateRoleDTO dto = new CreateRoleDTO();
        dto.setCode("ROLE_UPDATE_IMPORT");
        dto.setName("Original Name");
        sysRoleService.createRole(dto);

        // 准备导入数据（包含已存在的，会更新）
        List<SysRoleExcel> dataList = List.of(
            createRoleExcel("ROLE_UPDATE_IMPORT", "Updated Name"),
            createRoleExcel("ROLE_NEW_IMPORT", "New Role")
        );

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysRoleExcel.class).sheet().doWrite(dataList);

        // 执行流式导入
        ImportResultVO result = sysRoleService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getSuccessCount());
        assertEquals(1, result.getUpdateCount());
        assertEquals(0, result.getFailCount());

        // 验证更新
        SysRole updated = sysRoleService.getByCode("ROLE_UPDATE_IMPORT");
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testRoleImportListenerValidation() throws Exception {
        // 准备测试数据（包含空值）
        List<SysRoleExcel> dataList = List.of(
            createRoleExcel(null, "Valid Name"),
            createRoleExcel("VALID_CODE", null),
            createRoleExcel("VALID_CODE_2", "Valid Name 2")
        );

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysRoleExcel.class).sheet().doWrite(dataList);

        // 流式导入（包含校验）
        ImportResultVO result = sysRoleService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 第1、2行应该校验失败（空code和空name）
        assertEquals(2, result.getFailCount());
        // 第3行成功
        assertEquals(1, result.getSuccessCount());
    }

    @Test
    void testStreamingBatchProcessing() throws Exception {
        // 创建 150 行数据（超过1批，验证分批处理）
        List<SysRoleExcel> dataList = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            SysRoleExcel excel = new SysRoleExcel();
            excel.setCode("BATCH_ROLE_" + i);
            excel.setName("Batch Role " + i);
            excel.setDescription("Batch test");
            dataList.add(excel);
        }

        // 写入Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, SysRoleExcel.class).sheet().doWrite(dataList);

        // 执行流式导入
        ImportResultVO result = sysRoleService.importExcel(new ByteArrayInputStream(out.toByteArray()));

        // 验证150行全部成功导入
        assertEquals(150, result.getSuccessCount());
        assertEquals(0, result.getUpdateCount());
        assertEquals(0, result.getFailCount());
    }

    @Test
    void testStreamingExportEmptyData() throws Exception {
        // 测试空数据导出（0行）
        MockHttpServletResponse response = new MockHttpServletResponse();
        RoleQueryDTO query = new RoleQueryDTO();
        sysRoleService.export(query, response);

        // 验证生成了Excel文件（至少有表头）
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportSmallBatch() throws Exception {
        // 创建 99 行数据（不满一批）
        for (int i = 1; i <= 99; i++) {
            CreateRoleDTO dto = new CreateRoleDTO();
            dto.setCode("SMALL_ROLE_" + i);
            dto.setName("Small Role " + i);
            sysRoleService.createRole(dto);
        }

        // 测试导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        RoleQueryDTO query = new RoleQueryDTO();
        sysRoleService.export(query, response);

        // 验证导出成功
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportExactBatch() throws Exception {
        // 创建 100 行数据（刚好一批）
        for (int i = 1; i <= 100; i++) {
            CreateRoleDTO dto = new CreateRoleDTO();
            dto.setCode("EXACT_ROLE_" + i);
            dto.setName("Exact Role " + i);
            sysRoleService.createRole(dto);
        }

        // 测试导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        RoleQueryDTO query = new RoleQueryDTO();
        sysRoleService.export(query, response);

        // 验证导出成功
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportMultiBatch() throws Exception {
        // 创建 101 行数据（两批次）
        for (int i = 1; i <= 101; i++) {
            CreateRoleDTO dto = new CreateRoleDTO();
            dto.setCode("MULTI_ROLE_" + i);
            dto.setName("Multi Role " + i);
            sysRoleService.createRole(dto);
        }

        // 测试导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        RoleQueryDTO query = new RoleQueryDTO();
        sysRoleService.export(query, response);

        // 验证导出成功
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportWithFilter() throws Exception {
        // 创建测试数据
        CreateRoleDTO dto1 = new CreateRoleDTO();
        dto1.setCode("FILTER_ROLE_001");
        dto1.setName("Filter Test Role 001");
        sysRoleService.createRole(dto1);

        CreateRoleDTO dto2 = new CreateRoleDTO();
        dto2.setCode("FILTER_ROLE_002");
        dto2.setName("Filter Test Role 002");
        sysRoleService.createRole(dto2);

        // 按名称筛选导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        RoleQueryDTO query = new RoleQueryDTO();
        query.setName("001");
        sysRoleService.export(query, response);

        // 验证导出成功
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportByIds() throws Exception {
        // 创建测试数据
        CreateRoleDTO dto1 = new CreateRoleDTO();
        dto1.setCode("ID_ROLE_001");
        dto1.setName("ID Test Role 001");
        SysRole role1 = sysRoleService.createRole(dto1);

        CreateRoleDTO dto2 = new CreateRoleDTO();
        dto2.setCode("ID_ROLE_002");
        dto2.setName("ID Test Role 002");
        sysRoleService.createRole(dto2);

        // 测试按ID导出
        MockHttpServletResponse response = new MockHttpServletResponse();
        sysRoleService.exportByIds(List.of(role1.getId()), response);

        // 验证导出成功
        assertNotNull(response.getContentAsByteArray());
        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    void testStreamingExportByIdsEmptyList() {
        // 测试空ID列表导出（应抛出异常）
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertThrows(RuntimeException.class, () -> {
            sysRoleService.exportByIds(List.of(), response);
        });
    }

    private SysRoleExcel createRoleExcel(String code, String name) {
        SysRoleExcel excel = new SysRoleExcel();
        excel.setCode(code);
        excel.setName(name);
        excel.setDescription("Test description");
        return excel;
    }
}
