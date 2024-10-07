package com.bulain.mybatis.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 *  缓存传递依赖
 */
@Slf4j
public class RedisMapping {

    private static final List<String> emptyList = new ArrayList<String>();
    private static final RedisMapping redisMapping = new RedisMapping();
    private final Map<String, List<String>> mapping = new HashMap<String, List<String>>();

    public static RedisMapping getInstance() {
        return redisMapping;
    }

    public RedisMapping() {
        Properties properties = new Properties();
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("redis-mapping.properties");
        } catch (IOException e) {
            log.warn("Can not found redis-mapping.properties", e);
        }

        Set<Entry<Object, Object>> entrySet = properties.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            String[] split = value.split(",");
            for (String str : split) {
                if (str != null && str.trim().length() > 0) {
                    List<String> list = mapping.get(str);
                    if (list == null) {
                        list = new ArrayList<String>();
                        mapping.put(str, list);
                    }
                    list.add(key);
                }
            }
        }

        //处理传递依赖
        boolean changed = true;
        while (changed) {
            changed = false;
            List<String[]> pairs = new ArrayList<String[]>();
            Set<Entry<String, List<String>>> mapEntrySet = mapping.entrySet();
            for (Entry<String, List<String>> entry : mapEntrySet) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                for (String value : values) {
                    if (mapping.containsKey(value)) {
                        pairs.add(new String[]{key, value});
                    }
                }
            }
            for (String[] pair : pairs) {
                List<String> orginal = mapping.get(pair[0]);
                List<String> dependency = mapping.get(pair[1]);
                for (String dep : dependency) {
                    if (!orginal.contains(dep)) {
                        changed = true;
                        orginal.add(dep);
                    }
                }
            }
        }
    }

    public List<String> getMapping(String key) {
        List<String> list = mapping.get(key);
        if (list == null) {
            list = emptyList;
        }
        return list;
    }

}
