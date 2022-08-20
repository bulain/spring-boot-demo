package com.bulain.caffeine;

import com.bulain.caffeine.pojo.User;
import com.bulain.caffeine.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CaffeineApplication.class)
public class UserServiceDemo {

    @Autowired
    private UserService userService;

    @Test
    public void testCache() {

        User user = new User();
        user.setId("101");
        user.setName("name");
        user.setText("text");
        userService.saveUser(user);

        user = userService.getUser("101");
        user = userService.getUser("101");
        user = userService.getUser("101");

        userService.deleteUser("101");

    }


}
