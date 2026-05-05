package com.bulain.mybatis.sys.service;

import java.io.OutputStream;

/**
 * Excel模板生成服务
 */
public interface ExcelTemplateService {

    /**
     * 下载用户导入模板
     *
     * @param outputStream 输出流
     */
    void downloadUserTemplate(OutputStream outputStream);

    /**
     * 下载角色导入模板
     *
     * @param outputStream 输出流
     */
    void downloadRoleTemplate(OutputStream outputStream);

}
