package com.bulain.webflux.ctrl;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.bulain.webflux.pojo.User;

public class UserControllerTest {

	@Test
	public void testUser() {
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build(); 
		webClient.get().uri("/user").accept(MediaType.APPLICATION_STREAM_JSON) 
				.exchange() 
				.flatMapMany(response -> response.bodyToFlux(User.class)) 
				.doOnNext(System.out::println) 
				.blockLast(); 
	}

}
