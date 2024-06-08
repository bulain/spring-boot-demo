package com.bulain.fastjson;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Disabled
class FastjsonTest {

    @Test
    @SneakyThrows
    void dynFieldToJSONString() {
        ClassPathResource resource = new ClassPathResource("tdata/100.json");
        try (InputStream is = resource.getInputStream()) {
            String json = IOUtils.toString(is, StandardCharsets.UTF_8);
            DynField data = JSON.parseObject(json, DynField.class);
            log.info("{}", data);
            Assertions.assertNotNull(data);

            String jsonv = JSON.toJSONString(data);
            log.info(jsonv);
            Assertions.assertNotNull(jsonv);
        }
    }

    @Test
    @SneakyThrows
    void dynAreaToJSONString() {
        ClassPathResource resource = new ClassPathResource("tdata/101.json");
        try (InputStream is = resource.getInputStream()) {
            String json = IOUtils.toString(is, StandardCharsets.UTF_8);
            DynArea data = JSON.parseObject(json, DynArea.class);
            log.info("{}", data);
            Assertions.assertNotNull(data);

            String jsonv = JSON.toJSONString(data);
            log.info(jsonv);
            Assertions.assertNotNull(jsonv);
        }
    }

    @Test
    @SneakyThrows
    void dynPageToJSONString() {
        ClassPathResource resource = new ClassPathResource("tdata/102.json");
        try (InputStream is = resource.getInputStream()) {
            String json = IOUtils.toString(is, StandardCharsets.UTF_8);
            DynPage data = JSON.parseObject(json, DynPage.class);
            log.info("{}", data);
            Assertions.assertNotNull(data);

            String jsonv = JSON.toJSONString(data);
            log.info(jsonv);
            Assertions.assertNotNull(jsonv);
        }
    }

}
