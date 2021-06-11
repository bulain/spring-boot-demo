package com.bulain.redis.dao;


import com.bulain.redis.model.Role;
import org.springframework.data.redis.core.RedisTemplate;

public class RoleDao extends RedisTemplate<String, Role> {
    
}
