package com.bulain.mybatis.sys.service;

import com.bulain.mybatis.sys.dto.BindWechatDTO;
import com.bulain.mybatis.sys.dto.UnbindWechatDTO;
import com.bulain.mybatis.sys.dto.WechatLoginDTO;
import com.bulain.mybatis.sys.dto.WechatQrCodeResponse;
import com.bulain.mybatis.sys.entity.SysUser;

import java.util.Map;

/**
 * 微信登录服务接口
 */
public interface WechatLoginService {

    /**
     * 获取微信登录二维码参数
     */
    WechatQrCodeResponse getQrCode();

    /**
     * 微信登录
     */
    Map<String, Object> wechatLogin(WechatLoginDTO dto);

    /**
     * 绑定微信
     */
    void bindWechat(String userId, BindWechatDTO dto);

    /**
     * 解绑微信
     */
    void unbindWechat(String userId, UnbindWechatDTO dto);

    /**
     * 获取微信绑定状态
     */
    Map<String, Object> getWechatStatus(String userId);

}
