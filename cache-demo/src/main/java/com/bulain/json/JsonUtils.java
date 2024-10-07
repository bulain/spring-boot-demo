package com.bulain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON辅助类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {

    private static final JsonMapper jsonMapper;
    private static final JsonMapper nullMapper;
    private static final ObjectWriter jsonWriter;
    private static final ObjectWriter nullWriter;

    static {
        //日期序列化方式
        LocalDateSerializer localDateSerializer = new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 序列化时不输出null
        jsonMapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .addModule(new JsonModule(localDateSerializer, localDateTimeSerializer, localDateDeserializer, localDateTimeDeserializer))
                .build();
        jsonWriter = jsonMapper.writer(SerializationFeature.INDENT_OUTPUT);

        // 序列化时输出null
        nullMapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .addModule(new JsonModule(localDateSerializer, localDateTimeSerializer, localDateDeserializer, localDateTimeDeserializer))
                .build();
        nullWriter = nullMapper.writer(SerializationFeature.INDENT_OUTPUT);

    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    @SneakyThrows
    public static String toJSON(Object object) {
        return jsonMapper.writeValueAsString(object);
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param object       对象
     * @param prettyFormat 是否格式化
     * @return JSON字符串
     */
    @SneakyThrows
    public static String toJSON(Object object, boolean prettyFormat) {
        if (prettyFormat) {
            return jsonWriter.writeValueAsString(object);
        } else {
            return jsonMapper.writeValueAsString(object);
        }
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param object       对象
     * @param prettyFormat 是否格式化
     * @param outputNull   是否输出null
     * @return JSON字符串
     */
    @SneakyThrows
    public static String toJSON(Object object, boolean prettyFormat, boolean outputNull) {
        if (outputNull) {
            if (prettyFormat) {
                return nullWriter.writeValueAsString(object);
            } else {
                return nullMapper.writeValueAsString(object);
            }
        } else {
            if (prettyFormat) {
                return jsonWriter.writeValueAsString(object);
            } else {
                return jsonMapper.writeValueAsString(object);
            }
        }
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param text JSON字符串
     * @return 对象
     */
    @SneakyThrows
    public static JsonNode parseObject(String text) {
        return jsonMapper.readTree(text);
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param text  JSON字符串
     * @param clazz 类
     * @return 对象
     */
    @SneakyThrows
    public static <T> T parseObject(String text, Class<T> clazz) {
        return jsonMapper.readValue(text, clazz);
    }

    /**
     * 将JSON字符串转换为对象(支持泛型)
     *
     * @param text JSON字符串
     * @param type 目标类
     * @return 对象
     */
    @SneakyThrows
    public static <T> T parseObject(String text, TypeReference<T> type) {
        return jsonMapper.readValue(text, type);
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param text  JSON字符串
     * @param clazz 类
     * @return 对象列表
     */
    @SneakyThrows
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        JavaType javaType = jsonMapper.getTypeFactory().constructParametricType(List.class, clazz);
        return jsonMapper.readValue(text, javaType);
    }

    /**
     * 转换Java对象为Json对象
     *
     * @param javaObject java对象
     * @return Json对象(JSONObject or JSONArray)
     */
    public static JsonNode toJsonObject(Object javaObject) {

        // 空值判断
        if (javaObject == null) {
            return null;
        }

        // 转换为Json对象
        return jsonMapper.valueToTree(javaObject);

    }

    /***
     * 转换Json对象为Java对象
     *
     * @param json Json对象(JSONObject or JSONArray)
     * @param clazz 目标类
     * @return Java对象
     */
    @SneakyThrows
    public static <T> T toJavaObject(JsonNode json, Class<T> clazz) {
        return jsonMapper.treeToValue(json, clazz);
    }

    /***
     * 转换包装对象
     *
     * @param value 消息参数
     * @return Java对象
     */
    @SneakyThrows
    public static JsonNode toWrapObject(Object value) {
        Map<String, Object> object = new HashMap<>();
        object.put("value", value);
        return jsonMapper.valueToTree(object);
    }

    private static class JsonModule extends SimpleModule {
        public JsonModule(LocalDateSerializer localDateSerializer, LocalDateTimeSerializer localDateTimeSerializer,
                          LocalDateDeserializer localDateDeserializer, LocalDateTimeDeserializer localDateTimeDeserializer) {
            addSerializer(LocalDate.class, localDateSerializer);
            addSerializer(LocalDateTime.class, localDateTimeSerializer);
            addDeserializer(LocalDate.class, localDateDeserializer);
            addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        }
    }
}
