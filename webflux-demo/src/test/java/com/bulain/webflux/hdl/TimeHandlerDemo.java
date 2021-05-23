package com.bulain.webflux.hdl;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class TimeHandlerDemo {

	@Test
    public void testTimes() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient.get().uri("/times")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .log()
                .take(10)
                .blockLast();
    }

}
