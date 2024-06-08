package com.bulain.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Disabled
class JsonUtilsTest {
    private static DynUser user;
    private static String text;

    @BeforeAll
    static void setUp() {
        user = new DynUser()
                .setName("test")
                .setBirth(LocalDate.now())
                .setTime(LocalDateTime.now())
                .setAge(20)
                .setHight(4000L)
                .setAmount(new BigDecimal(20000));
        text = "{\"name\":\"test\",\"birth\":\"2024-06-09\",\"time\":\"2024-06-09 00:24:18\",\"age\":20,\"hight\":4000,\"amount\":20000}";
    }

    @Test
    void toJSON() {
        String json = JsonUtils.toJSON(user);
        System.out.println(json);
        Assertions.assertTrue(true);
    }

    @Test
    void toJSON4Array() {
        List<DynUser> users = Arrays.asList(user, user);
        String json = JsonUtils.toJSON(users);
        System.out.println(json);
        Assertions.assertTrue(true);
    }

    @Test
    void toJSON4Pretty() {
        String json = JsonUtils.toJSON(user, true);
        System.out.println(json);
        Assertions.assertTrue(true);
    }

    @Test
    void toJSON4PrettyNull() {
        String json = JsonUtils.toJSON(user, true, true);
        System.out.println(json);
        Assertions.assertTrue(true);
    }

    @Test
    void parseObject() {
        JsonNode obj = JsonUtils.parseObject(text);
        System.out.println(obj);
        Assertions.assertTrue(true);
    }

    @Test
    void parseObject4Clazz() {
        DynUser obj = JsonUtils.parseObject(text, DynUser.class);
        System.out.println(obj);
        Assertions.assertTrue(true);
    }

    @Test
    void parseObject4Array() {
        String texts = "[" + text + "," + text + "]";
        DynUser[] obj = JsonUtils.parseObject(texts, DynUser[].class);
        System.out.println(Arrays.asList(obj));
        Assertions.assertTrue(true);
    }

    @Test
    void parseObject4Type() {
        DynUser javaObject = JsonUtils.parseObject(text, new TypeReference<>() {
        });
        System.out.println(javaObject);
        Assertions.assertTrue(true);
    }

    @Test
    void parseObject4List() {
        String texts = "[" + text + "," + text + "]";
        List<DynUser> javaObject = JsonUtils.parseObject(texts, new TypeReference<>() {
        });
        System.out.println(javaObject);
        Assertions.assertTrue(true);
    }

    @Test
    void toJsonObject() {
        JsonNode obj = JsonUtils.toJsonObject(user);
        System.out.println(obj);
        Assertions.assertTrue(true);
    }

    @Test
    void toJavaObject() {
        JsonNode obj = JsonUtils.toJsonObject(user);
        DynUser javaObject = JsonUtils.toJavaObject(obj, DynUser.class);
        System.out.println(javaObject);
        Assertions.assertTrue(true);
    }

    @Test
    void toWrapObject() {
        JsonNode wrapObject = JsonUtils.toWrapObject(user);
        System.out.println(wrapObject);
        Assertions.assertTrue(true);
    }

}