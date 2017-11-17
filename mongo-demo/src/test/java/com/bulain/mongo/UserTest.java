package com.bulain.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bulain.mongo.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MongoApplication.class)
public class UserTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void testSend() {

		User user = new User();
		user.setName("name");
		user.setText("text");
		mongoTemplate.save(user);

	}

}
