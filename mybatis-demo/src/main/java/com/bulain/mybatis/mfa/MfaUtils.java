package com.bulain.mybatis.mfa;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
    public static boolean checkCode(String secret, long code) {
        byte[] decodedKey = BASE32.decode(secret);
        long time = (System.currentTimeMillis() / 1000L) / 30L;
        long hash;
        for (int i = -WINDOW_SIZE; i <= WINDOW_SIZE; ++i) {
            try {
                hash = verifyCode(decodedKey, time + i);
            } catch (Exception e) {
                return false;
            }
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据时间偏移量计算
     */
    @SneakyThrows
    private static long verifyCode(byte[] key, long t) {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, CRYPTO);
        Mac mac = Mac.getInstance(CRYPTO);
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return truncatedHash;
    }

}
