package com.bulain.webflux.ctrl;

import java.time.Duration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.bulain.webflux.pojo.Event;

import reactor.core.publisher.Flux;

public class EventControllerTest {

	@Test
    public void testEvents() {
        Flux<Event> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(l -> new Event(System.currentTimeMillis(), "message-" + l)).take(10);
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
                .post().uri("/events")
                .contentType(MediaType.APPLICATION_STREAM_JSON) 
                .body(eventFlux, Event.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
