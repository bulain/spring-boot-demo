package com.bulain.caffeine.service;

import com.bulain.caffeine.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserService {
    private Map<String, User> mapUser = new ConcurrentHashMap<>();

    @Cacheable(value = "user", key = "#p0")
    public User getUser(String id) {
        log.info("getUser({})", id);
        return mapUser.get(id);
    }

    @CacheEvict(value = "user", key = "#p0.id")
    public void saveUser(User user) {
        log.info("saveUser({})", user.getId());
        mapUser.put(user.getId(), user);
    }

    @CacheEvict(value = "user", key = "#p0")
    public void deleteUser(String id) {
        log.info("deleteUser({})", id);
        mapUser.remove(id);
    }

}
