package com.bulain.mime.ctrl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.RandomUtil;
import com.bulain.mime.pojo.DataResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mime")
public class InitCtrl {

    @GetMapping(value = "init")
    public DataResp<String> init() {

        AbstractCaptcha captcha = CaptchaUtil.createShearCaptcha(120, 40);
        captcha.setGenerator(new RandomGenerator(RandomUtil.BASE_NUMBER, 5));
        String image = captcha.getImageBase64Data();

        return DataResp.ok(image);
    }

}
