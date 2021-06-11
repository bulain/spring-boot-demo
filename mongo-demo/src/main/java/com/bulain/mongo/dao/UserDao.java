package com.bulain.mongo.dao;

import com.bulain.mongo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<User, String> {
}
