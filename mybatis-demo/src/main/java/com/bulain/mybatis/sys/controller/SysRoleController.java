package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.common.Result;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RolePermissionAssignDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.excel.ImportResultVO;
import com.bulain.mybatis.sys.service.ExcelTemplateService;
import com.bulain.mybatis.sys.service.SysRoleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/sys/roles")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private ExcelTemplateService excelTemplateService;

    @PostMapping
    public Result<SysRole> createRole(@RequestBody CreateRoleDTO dto) {
        SysRole role = sysRoleService.createRole(dto);
        return Result.success(role);
    }

    @GetMapping("/{id}")
    public Result<SysRole> getRoleById(@PathVariable("id") String id) {
        SysRole role = sysRoleService.getById(id);
        return Result.success(role);
    }

    @PutMapping("/{id}")
    public Result<SysRole> updateRole(@PathVariable("id") String id, @RequestBody UpdateRoleDTO dto) {
        SysRole role = sysRoleService.updateRole(id, dto);
        return Result.success(role);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable("id") String id) {
        sysRoleService.removeById(id);
        return Result.success();
    }

    @GetMapping
    public Result<Paged<SysRole>> listRoles(RoleQueryDTO query) {
        Paged<SysRole> page = sysRoleService.pageRoles(query);
        return Result.success(page);
    }

    @GetMapping("/{id}/permissions")
    public Result<List<SysPermission>> getRolePermissions(@PathVariable("id") String id) {
        List<SysPermission> permissions = sysRoleService.getRolePermissions(id);
        return Result.success(permissions);
    }

    @PutMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable("id") String id, @RequestBody RolePermissionAssignDTO dto) {
        sysRoleService.assignPermissions(id, dto.getPermissionIds());
        return Result.success();
    }

    /**
     * 按条件导出角色
     */
    @PostMapping("/export")
    public void exportRoles(@RequestBody RoleQueryDTO query, HttpServletResponse response) {
        sysRoleService.export(query, response);
    }

    /**
     * 按ID导出角色
     */
    @PostMapping("/export-by-ids")
    public void exportRolesByIds(@RequestBody List<String> ids, HttpServletResponse response) {
        sysRoleService.exportByIds(ids, response);
    }

    /**
     * 导入角色（流式分批处理，避免OOM）
     */
    @PostMapping("/import")
    public Result<ImportResultVO> importRoles(@RequestParam("file") MultipartFile file) throws IOException {
        ImportResultVO result = sysRoleService.importExcel(file.getInputStream());
        return Result.success(result);
    }

    /**
     * 下载角色导入模板
     * 需管理员权限
     */
    @GetMapping("/template")
    public void downloadRoleTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("角色导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        excelTemplateService.downloadRoleTemplate(response.getOutputStream());
    }

}
