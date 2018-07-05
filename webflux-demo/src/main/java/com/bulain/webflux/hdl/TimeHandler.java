package com.bulain.webflux.hdl;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TimeHandler {
    public Mono<ServerResponse> getTime(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Now is " + new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);
    }
    public Mono<ServerResponse> getDate(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Today is " + new SimpleDateFormat("yyyy-MM-dd").format(new Date())), String.class);
    }
	public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(
				Flux.interval(Duration.ofSeconds(1)).map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date())),
				String.class);
	}
}
