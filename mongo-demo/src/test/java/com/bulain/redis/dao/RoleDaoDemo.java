package com.bulain.redis.dao;

import com.bulain.mongo.model.User;
import com.bulain.redis.RedisApplication;
import com.bulain.redis.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RedisApplication.class)
public class RoleDaoDemo {

    @Autowired
    private RoleDao roleDao;

    @Test
    public void testSave() {
        Role role = new Role();
        role.setId("1001");
        role.setName("name");
        roleDao.opsForValue().set(role.getId(), role);
    }
}
