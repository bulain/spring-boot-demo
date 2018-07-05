package com.bulain.webflux.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import com.bulain.webflux.pojo.Event;

import reactor.core.publisher.Flux;

public interface EventRepository extends ReactiveMongoRepository<Event, Long> {
	@Tailable
	Flux<Event> findBy();
}
