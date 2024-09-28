package com.bulain.mybatis.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis服务的实现类。
 */
@SuppressWarnings("unchecked")
public class RedisServiceImpl implements RedisService {

    private RedisTemplate<Object, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Set<String> keys(String pattern) {
        Set<Object> keys = redisTemplate.keys(pattern);
        Set<String> list = new HashSet<String>();
        for (Object obj : keys) {
            list.add(obj.toString());
        }
        return list;
    }

    @Override
    public void set(String key, Object obj, int seconds) {
        redisTemplate.opsForValue().set(key, obj, seconds, TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> List<T> mget(Collection<String> keys) {
        Collection<Object> objs = new LinkedList<Object>(keys);
        return (List<T>) redisTemplate.opsForValue().multiGet(objs);
    }

    @Override
    public void del(String... key) {
        List<Object> objs = new LinkedList<Object>();
        for (String k : key) {
            objs.add(k);
        }
        redisTemplate.delete(objs);
    }

    @Override
    public void hset(String key, String field, Object obj, int seconds) {
        redisTemplate.opsForHash().put(key, field, obj);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    @Override
    public <T> T hget(String key, String field) {
        return (T) redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public <T> List<T> hmget(String key, Collection<String> fields) {
        Collection<Object> hashKeys = new LinkedList<Object>(fields);
        return (List<T>) redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    @Override
    public void hdel(String key, String... fields) {
        Object[] objs = null;
        if (fields != null) {
            objs = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                objs[i] = fields[i];
            }
        }
        redisTemplate.opsForHash().delete(key, objs);
    }

    @Override
    public Long hlen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public void expire(String key, int seconds) {
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

}
