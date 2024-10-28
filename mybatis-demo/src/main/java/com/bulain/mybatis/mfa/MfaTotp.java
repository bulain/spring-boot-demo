package com.bulain.mybatis.mfa;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 验证码生成工具类
 */
public class MfaTotp {
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /**
     * This method uses the JCE to provide the crypto algorithm. HMAC computes a
     * Hashed Message Authentication Code with the crypto hash algorithm as a
     * parameter.
     *
     * @param crypto   : the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes : the bytes to use for the HMAC key
     * @param text     : the message or text to be authenticated
     */
    @SneakyThrows
    private static byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text) {
        Mac hmac = Mac.getInstance(crypto);
        SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
        hmac.init(macKey);
        return hmac.doFinal(text);
    }

    /**
     * This method converts a HEX string to Byte[]
     *
     * @param hex : the HEX string
     * @return a byte array
     */
    @SneakyThrows
    private static byte[] hexStr2Bytes(String hex) {
        return Hex.decodeHex(hex);
    }

    /**
     * This method generates a TOTP value for the given set of parameters.
     *
     * @param key          : the shared secret, HEX encoded
     * @param time         : a value that reflects a time
     * @param returnDigits : number of digits to return
     * @param crypto       : the crypto function to use
     * @return a numeric String in base 10 that includes
     */
    public static String generateTOTP(String key, String time, int returnDigits, String crypto) {
        String result = null;
        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        while (time.length() < 16) {
            time = "0" + time;
        }
        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(time);
        byte[] k = hexStr2Bytes(key);
        byte[] hash = hmacSha(crypto, k, msg);
        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        int otp = binary % DIGITS_POWER[returnDigits];
        result = Integer.toString(otp);
        while (result.length() < returnDigits) {
            result = "0" + result;
        }
        return result;
    }

}