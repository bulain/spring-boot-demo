package com.bulain.mybatis.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/***
 * Redis缓存实现
 */
@Slf4j
public class RedisCache implements Cache {
    private static final String PREFIX_MYBATIS = "mybatis:";
    private static final int MYBATIS_EXPIRES_IN = 30;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;
    private RedisService redisService;

    public RedisCache(final String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public int getSize() {
        log.debug("getSize: {}", id);
        Long hlen = redisService().hlen(getHkey(id));
        return Integer.valueOf(hlen.toString());
    }

    public void putObject(Object key, Object value) {
        log.debug("putObject: {}/{}/{}", id, key, value);
        redisService().hset(getHkey(id), getHfield(key), value, expireSeconds());
    }

    public Object getObject(Object key) {
        log.debug("getObject: {}/{}", id, key);
        return redisService().hget(getHkey(id), getHfield(key));
    }

    public Object removeObject(Object key) {
        log.debug("removeObject: {}/{}", id, key);
        Object value = redisService().hget(getHkey(id), getHfield(key));
        redisService().hdel(getHkey(id), getHfield(key));
        return value;
    }

    public void clear() {
        log.debug("clear: {}", id);
        List<String> mapping = RedisMapping.getInstance().getMapping(id);
        List<String> hkeys = new ArrayList<String>();
        hkeys.add(getHkey(id));
        for (String str : mapping) {
            String oHkey = getHkey(str);
            hkeys.add(oHkey);
        }
        redisService().del(hkeys.toArray(new String[0]));
    }

    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    private RedisService redisService() {
        if (redisService == null) {
            redisService = RedisContextHolder.getRedisService();
        }
        return redisService;
    }

    private String getHkey(String str) {
        return PREFIX_MYBATIS + str;
    }

    private String getHfield(Object key) {
        return String.valueOf(key.hashCode());
    }

    private int expireSeconds() {
        return MYBATIS_EXPIRES_IN * 60;
    }

}
