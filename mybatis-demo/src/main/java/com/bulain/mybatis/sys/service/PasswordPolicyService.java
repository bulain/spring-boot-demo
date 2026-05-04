package com.bulain.mybatis.sys.service;

/**
 * 密码策略服务接口
 */
public interface PasswordPolicyService {

    /**
     * 验证密码是否符合策略要求
     *
     * @param password 密码
     * @param username 用户名（用于检查密码是否包含用户名）
     * @return 验证结果，null 表示通过，否则返回错误信息
     */
    String validatePassword(String password, String username);

}
