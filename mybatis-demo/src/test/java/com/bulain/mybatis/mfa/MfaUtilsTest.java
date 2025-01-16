package com.bulain.mybatis.mfa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class MfaUtilsTest {

    @Test
    void test() {
        for (int i = 0; i < 10; i++) {
            String secretKey = MfaUtils.getSecretKey();
            log.info("secretKey：{}", secretKey);

            String qrCodeText = MfaUtils.getQrCodeText(secretKey, "test", "demo");
            log.info("qrCodeText：{}", qrCodeText);

            String code = MfaUtils.getCode(secretKey);
            log.info("code：{}", code);

            boolean b = MfaUtils.checkCode(secretKey, code);
            log.info("isSuccess：{}", b);
        }
    }

}