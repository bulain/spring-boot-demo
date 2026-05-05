package com.bulain.mybatis.sys.service;

import com.bulain.mybatis.sys.dto.BindDingtalkDTO;
import com.bulain.mybatis.sys.dto.DingtalkQrCodeResponse;
import com.bulain.mybatis.sys.dto.DingtalkLoginDTO;
import com.bulain.mybatis.sys.dto.UnbindDingtalkDTO;

import java.util.Map;

/**
 * 钉钉登录服务接口
 */
public interface DingtalkLoginService {

    /**
     * 获取钉钉登录二维码参数
     */
    DingtalkQrCodeResponse getQrCode();

    /**
     * 钉钉登录
     */
    Map<String, Object> dingtalkLogin(DingtalkLoginDTO dto);

    /**
     * 绑定钉钉
     */
    void bindDingtalk(String userId, BindDingtalkDTO dto);

    /**
     * 解绑钉钉
     */
    void unbindDingtalk(String userId, UnbindDingtalkDTO dto);

    /**
     * 获取钉钉绑定状态
     */
    Map<String, Object> getDingtalkStatus(String userId);

}
