package com.bulain.mybatis.sys.service.impl;

import com.alibaba.excel.EasyExcel;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.excel.SysUserExcel;
import com.bulain.mybatis.sys.excel.SysUserReadListener;
import com.bulain.mybatis.sys.service.SysExcelService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel导入导出服务实现类
 */
@Service
public class SysExcelServiceImpl implements SysExcelService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void exportUsers(OutputStream outputStream, List<SysUserExcel> data) {
        EasyExcel.write(outputStream, SysUserExcel.class)
                .sheet("用户列表")
                .doWrite(data);
    }

    @Override
    public List<SysUser> importUsers(InputStream inputStream) {
        SysUserReadListener listener = new SysUserReadListener();
        EasyExcel.read(inputStream, SysUserExcel.class, listener).sheet().doRead();

        return listener.getDataList().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void downloadUserTemplate(OutputStream outputStream) {
        List<SysUserExcel> templateData = new ArrayList<>();
        // 添加示例数据
        SysUserExcel example = new SysUserExcel();
        example.setUsername("zhangsan");
        example.setName("张三");
        example.setEmail("zhangsan@example.com");
        example.setPhone("13800138000");
        example.setStatus("启用");
        templateData.add(example);

        EasyExcel.write(outputStream, SysUserExcel.class)
                .sheet("用户导入模板")
                .doWrite(templateData);
    }

    private SysUser convertToEntity(SysUserExcel excel) {
        SysUser user = new SysUser();
        user.setUsername(excel.getUsername());
        user.setName(excel.getName());
        user.setEmail(excel.getEmail());
        user.setPhone(excel.getPhone());
        // 默认密码为手机号后6位
        String defaultPassword = excel.getPhone() != null && excel.getPhone().length() >= 6
                ? excel.getPhone().substring(excel.getPhone().length() - 6)
                : "123456";
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setStatus("启用".equals(excel.getStatus()) ? 1 : 0);
        return user;
    }

}
