package com.bulain.mybatis.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***
 * Redis环境参数
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisContextHolder {

    private static RedisService redisService;

    public static void setRedisService(RedisService redisService) {
        RedisContextHolder.redisService = redisService;
    }

    public static RedisService getRedisService() {
        return RedisContextHolder.redisService;
    }

}
