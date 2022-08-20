package com.bulain.redis;

import com.bulain.caffeine.pojo.User;
import com.bulain.redis.pojo.RedisUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RedisApplication.class)
public class CacheRedisDemo {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCache() {

        Cache cache = cacheManager.getCache("user");

        RedisUser user = new RedisUser()
                .setUserId("101")
                .setUserName("name")
                .setUserAge(20)
                .setDt(new Date())
                .setLdt(LocalDateTime.now())
                .setLd(LocalDate.now());
        cache.put("101", user);

        RedisUser ret = cache.get("101", RedisUser.class);
        Assertions.assertEquals(user, ret);

    }

}
