package com.bulain.webflux.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.bulain.webflux.pojo.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
	Mono<User> findByUsername(String username);
	Mono<Long> deleteByUsername(String username);
}
