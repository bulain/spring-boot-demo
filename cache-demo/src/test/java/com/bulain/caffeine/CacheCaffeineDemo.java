package com.bulain.caffeine;

import com.bulain.caffeine.pojo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CaffeineApplication.class)
public class CacheCaffeineDemo {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCache() {

        Cache cache = cacheManager.getCache("user");

        User user = new User();
        user.setId("101");
        user.setName("name");
        user.setText("text");
        cache.put("101", user);

        User ret = cache.get("101", User.class);
        Assertions.assertEquals(user, ret);

    }

}
