package com.bulain.webflux.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bulain.webflux.pojo.User;
import com.bulain.webflux.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("")
	public Mono<User> save(User user) {
		return this.userService.save(user);
	}

	@DeleteMapping("/{username}")
	public Mono<Long> deleteByUsername(@PathVariable String username) {
		return this.userService.deleteByUsername(username);
	}

	@GetMapping("/{username}")
	public Mono<User> findByUsername(@PathVariable String username) {
		return this.userService.findByUsername(username);
	}

	@GetMapping("")
	public Flux<User> findAll() {
		return this.userService.findAll();
	}
}
