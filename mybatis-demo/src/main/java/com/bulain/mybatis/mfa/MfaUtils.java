package com.bulain.mybatis.mfa;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.math.NumberUtils;

import java.security.SecureRandom;

/**
 * 谷歌身份验证器工具类
 */
public class MfaUtils {
    /**
     * 时间前后偏移量
     * 用于防止客户端时间不精确导致生成的TOTP与服务器端的TOTP一直不一致
     * 如果为0,当前时间为 10:10:15
     * 则表明在 10:10:00-10:10:30 之间生成的TOTP 能校验通过
     * 如果为1,则表明在
     * 10:09:30-10:10:00
     * 10:10:00-10:10:30
     * 10:10:30-10:11:00 之间生成的TOTP 能校验通过
     * 以此类推
     */
    private static final int WINDOW_SIZE = 1;
    /**
     * 加密方式，HmacSHA1、HmacSHA256、HmacSHA512
     */
    private static final String CRYPTO = "HmacSHA1";
    /**
     * 随机数
     */
    private static final SecureRandom RANDOM = new SecureRandom();
    /**
     * BASE32位
     */
    private static final Base32 BASE32 = new Base32();

    /**
     * 生成密钥，每个用户独享一份密钥
     */
    public static String getSecretKey() {
        byte[] bytes = new byte[20];
        RANDOM.nextBytes(bytes);
        return BASE32.encodeToString(bytes);
    }

    /**
     * 生成二维码内容
     *
     * @param secretKey 密钥
     * @param account   账户名
     * @param issuer    网站地址（可不写）
     */
    public static String getQrCodeText(String secretKey, String account, String issuer) {
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, account, secretKey, issuer);
    }

    /**
     * 获取验证码
     */
    public static String getCode(String secretKey) {
        byte[] bytes = BASE32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        long time = (System.currentTimeMillis() / 1000L) / 30L;
        String hexTime = Long.toHexString(time);
        return MfaTotp.generateTOTP(hexKey, hexTime, 6, CRYPTO);
    }

    /**
     * 检验验证码是否正确
     */
    public static boolean checkCode(String secretKey, String code) {
        long lcode = NumberUtils.toLong(code);
        long time = (System.currentTimeMillis() / 1000L) / 30L;
        long hash;
        for (int i = -WINDOW_SIZE; i <= WINDOW_SIZE; ++i) {
            try {
                hash = MfaTotp.verifyTOTP(secretKey, time + i, CRYPTO);
            } catch (Exception e) {
                return false;
            }
            if (hash == lcode) {
                return true;
            }
        }
        return false;
    }


}
