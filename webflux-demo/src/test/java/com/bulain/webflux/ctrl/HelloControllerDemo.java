package com.bulain.webflux.ctrl;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class HelloControllerDemo {

	@Test
	public void testHello() throws InterruptedException {
		WebClient webClient = WebClient.create("http://localhost:8080");
		Mono<String> resp = webClient.get().uri("/hello").retrieve().bodyToMono(String.class);
		resp.subscribe(System.out::println);
		resp.block();
	}

}
