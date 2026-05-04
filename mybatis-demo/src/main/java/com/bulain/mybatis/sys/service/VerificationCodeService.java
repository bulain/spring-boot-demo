package com.bulain.mybatis.sys.service;

import com.bulain.mybatis.sys.dto.ImageCaptchaResponse;

/**
 * 验证码服务接口
 */
public interface VerificationCodeService {

    /**
     * 生成图片验证码
     *
     * @param ip 请求 IP 地址
     * @return 图片验证码响应
     */
    ImageCaptchaResponse generateImageCaptcha(String ip);

    /**
     * 验证图片验证码
     *
     * @param captchaId 验证码 ID
     * @param captchaCode 用户输入的验证码
     * @return true 验证通过，false 验证失败
     */
    boolean validateImageCaptcha(String captchaId, String captchaCode);

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param ip 请求 IP 地址
     */
    void sendSmsCode(String phone, String ip);

    /**
     * 验证短信验证码
     *
     * @param phone 手机号
     * @param code 用户输入的验证码
     * @return true 验证通过，false 验证失败
     */
    boolean validateSmsCode(String phone, String code);
}
