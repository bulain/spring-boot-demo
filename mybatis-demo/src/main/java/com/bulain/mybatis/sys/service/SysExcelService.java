package com.bulain.mybatis.sys.service;

import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.excel.SysUserExcel;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Excel导入导出服务
 */
public interface SysExcelService {

    /**
     * 导出用户数据
     */
    void exportUsers(OutputStream outputStream, List<SysUserExcel> data);

    /**
     * 导入用户数据
     */
    List<SysUser> importUsers(InputStream inputStream);

    /**
     * 获取用户导入模板
     */
    void downloadUserTemplate(OutputStream outputStream);

}
