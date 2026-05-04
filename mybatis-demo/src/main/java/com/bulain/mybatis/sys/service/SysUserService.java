package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.UpdateUserDTO;
import com.bulain.mybatis.sys.dto.UserQueryDTO;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 创建用户
     */
    SysUser createUser(CreateUserDTO dto);

    /**
     * 更新用户
     */
    SysUser updateUser(String id, UpdateUserDTO dto);

    /**
     * 启用/禁用用户
     */
    void toggleStatus(String id, Integer status);

    /**
     * 重置密码
     */
    void resetPassword(String id, String newPassword);

    /**
     * 查询用户角色列表
     */
    List<SysRole> getUserRoles(String userId);

    /**
     * 分配用户角色
     */
    void assignRoles(String userId, List<String> roleIds);

    /**
     * 获取用户权限编码列表
     */
    Set<String> getUserPermissionCodes(String userId);

    /**
     * 根据用户名查询用户
     */
    SysUser getByUsername(String username);

    /**
     * 根据手机号查询用户
     */
    SysUser getByPhone(String phone);

    /**
     * 根据微信OpenID查询用户
     */
    SysUser getByWechatOpenid(String openid);

    /**
     * 根据钉钉UserID查询用户
     */
    SysUser getByDingtalkUserid(String userid);

    /**
     * 分页查询用户列表
     */
    Paged<SysUser> pageUsers(UserQueryDTO query);

    /**
     * 解锁账户（清除登录失败计数和锁定状态）
     */
    void unlockUser(String userId);

    /**
     * 修改密码
     */
    void changePassword(String userId, String oldPassword, String newPassword);

}