package com.bulain.mybatis.sys.service;

/**
 * 登录安全服务接口
 */
public interface LoginSecurityService {

    /**
     * 记录登录失败
     *
     * @param userId 用户ID
     * @param ip IP地址
     */
    void recordLoginFailure(String userId, String ip);

    /**
     * 记录登录成功（重置计数）
     *
     * @param userId 用户ID
     * @param ip IP地址
     */
    void recordLoginSuccess(String userId, String ip);

    /**
     * 检查账户是否被锁定
     *
     * @param userId 用户ID
     * @return true 表示被锁定
     */
    boolean isUserLocked(String userId);

    /**
     * 检查 IP 是否被限流
     *
     * @param ip IP地址
     * @return true 表示被限流
     */
    boolean isIpLimited(String ip);

    /**
     * 检查是否需要验证码
     *
     * @param userId 用户ID（可为 null）
     * @param ip IP地址
     * @return true 表示需要验证码
     */
    boolean isCaptchaRequired(String userId, String ip);

    /**
     * 手动解锁账户
     *
     * @param userId 用户ID
     */
    void unlockUser(String userId);

    /**
     * 检查账户是否达到需要验证码的阈值
     *
     * @param userId 用户ID
     * @return true 表示达到阈值
     */
    boolean isUserCaptchaThresholdReached(String userId);

    /**
     * 检查 IP 是否达到需要验证码的阈值
     *
     * @param ip IP地址
     * @return true 表示达到阈值
     */
    boolean isIpCaptchaThresholdReached(String ip);

}
