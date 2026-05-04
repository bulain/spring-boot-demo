package com.bulain.mybatis.sys.service;

/**
 * JWT服务接口
 */
public interface SysJwtService {

    /**
     * 生成Token
     */
    String generateToken(Long userId, String username);

    /**
     * 从Token中获取用户ID
     */
    Long getUserIdFromToken(String token);

    /**
     * 从Token中获取用户名
     */
    String getUsernameFromToken(String token);

    /**
     * 验证Token是否有效
     */
    boolean validateToken(String token);

    /**
     * 检查Token是否过期
     */
    boolean isTokenExpired(String token);

}
