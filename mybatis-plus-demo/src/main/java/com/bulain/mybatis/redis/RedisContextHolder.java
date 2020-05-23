package com.bulain.mybatis.redis;

/***
 * Redis环境参数
 */
public class RedisContextHolder {

    private static RedisService redisService;

    public RedisContextHolder(RedisService service) {
        redisService = service;
    }

    public static RedisService getRedisService() {
        return redisService;
    }

}
