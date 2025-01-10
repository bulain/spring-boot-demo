package com.bulain.jackson;

import com.bulain.fastjson.DynField;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Slf4j
@Disabled
class RedisSerializerTest {

    private RedisSerializer<Object> serializer;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTypingAsProperty(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.EVERYTHING, "@type");
        this.serializer = new GenericJackson2JsonRedisSerializer(mapper);
    }

    @Test
    void testList() {
        List<DynField> list = List.of();
        byte[] bytes = serializer.serialize(list);
        Assertions.assertNotNull(bytes);
        log.info("bytes: {}", new String(bytes, StandardCharsets.UTF_8));
        Object ret = serializer.deserialize(bytes);
        Assertions.assertNotNull(ret);
        log.info("ret: {}", ret);
    }

    @Test
    void testSet() {
        Set<DynField> list = Set.of();
        byte[] bytes = serializer.serialize(list);
        Assertions.assertNotNull(bytes);
        log.info("bytes: {}", new String(bytes, StandardCharsets.UTF_8));
        Object ret = serializer.deserialize(bytes);
        Assertions.assertNotNull(ret);
        log.info("ret: {}", ret);
    }

    @Test
    void testObject() {
        DynField obj = new DynField();
        byte[] bytes = serializer.serialize(obj);
        Assertions.assertNotNull(bytes);
        log.info("bytes: {}", new String(bytes, StandardCharsets.UTF_8));
        Object ret = serializer.deserialize(bytes);
        Assertions.assertNotNull(ret);
        log.info("ret: {}", ret);
    }

    @Test
    void testArray() {
        List<DynField> list = List.of(new DynField().setFname("fname"));
        byte[] bytes = serializer.serialize(list);
        Assertions.assertNotNull(bytes);
        log.info("bytes: {}", new String(bytes, StandardCharsets.UTF_8));
        Object ret = serializer.deserialize(bytes);
        Assertions.assertNotNull(ret);
        log.info("ret: {}", ret);
    }

    @Test
    void testSets() {
        Set<DynField> list = Set.of(new DynField().setFname("fname"));
        byte[] bytes = serializer.serialize(list);
        Assertions.assertNotNull(bytes);
        log.info("bytes: {}", new String(bytes, StandardCharsets.UTF_8));
        Object ret = serializer.deserialize(bytes);
        Assertions.assertNotNull(ret);
        log.info("ret: {}", ret);
    }

}
