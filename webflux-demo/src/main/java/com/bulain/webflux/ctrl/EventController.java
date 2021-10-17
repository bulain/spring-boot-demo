package com.bulain.webflux.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bulain.webflux.dao.EventRepository;
import com.bulain.webflux.pojo.Event;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventRepository eventRepository;

    @PostMapping(path = "")
    public Mono<Void> loadEvents(@RequestBody Flux<Event> events) {
        return eventRepository.insert(events).then();
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Event> getEvents() {
    	return eventRepository.findBy();
    }
}
