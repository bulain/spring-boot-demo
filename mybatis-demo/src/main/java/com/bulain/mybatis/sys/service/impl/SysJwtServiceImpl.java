package com.bulain.mybatis.sys.service.impl;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.bulain.mybatis.sys.service.SysJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT服务实现类（基于Hutool）
 */
@Service
public class SysJwtServiceImpl implements SysJwtService {

    @Value("${jwt.secret:rbac-demo-secret-key-2024-11-11-long-enough-for-hs256}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration; // 默认24小时

    private byte[] getSigningKeyBytes() {
        return secret.getBytes(StandardCharsets.UTF_8);
    }

    private JWTSigner getSigner() {
        return JWTSignerUtil.hs256(getSigningKeyBytes());
    }

    @Override
    public String generateToken(String userId, String username) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("sub", username);
        payload.put("iat", System.currentTimeMillis() / 1000);
        payload.put("exp", (System.currentTimeMillis() + expiration) / 1000);
        return JWTUtil.createToken(payload, getSigningKeyBytes());
    }

    @Override
    public String getUserIdFromToken(String token) {
        JWT jwt = JWTUtil.parseToken(token).setSigner(getSigner());
        return jwt.getPayload("userId").toString();
    }

    @Override
    public String getUsernameFromToken(String token) {
        JWT jwt = JWTUtil.parseToken(token).setSigner(getSigner());
        return jwt.getPayload("sub").toString();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return JWTUtil.verify(token, getSigningKeyBytes()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        JWT jwt = JWTUtil.parseToken(token).setSigner(getSigner());
        Date expiration = jwt.getPayloads().getDate("exp");
        return expiration.before(new Date());
    }

}
