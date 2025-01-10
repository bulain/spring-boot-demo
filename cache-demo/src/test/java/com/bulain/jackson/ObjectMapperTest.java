package com.bulain.jackson;

import com.bulain.fastjson.DynField;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class ObjectMapperTest {

    @Test
    @SneakyThrows
    public void testWriteValueAsString() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY
        );

        log.info("RET: {}", mapper.writeValueAsString(new DynField().setFname("fname")));
        log.info("RET: {}", mapper.writeValueAsString(List.of()));

    }


}
