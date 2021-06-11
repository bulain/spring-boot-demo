package com.bulain.mongo.dao;

import com.bulain.mongo.MongoApplication;
import com.bulain.mongo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MongoApplication.class)
public class UserDaoDemo {

    @Autowired
    private UserDao userDao;

    @Test
    public void testSave() {
        User user = new User();
        user.setName("name");
        user.setText("text");
        userDao.save(user);
    }
}
