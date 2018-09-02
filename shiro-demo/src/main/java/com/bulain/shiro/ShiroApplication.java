package com.bulain.shiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableCaching(proxyTargetClass = true)
@EnableRedisHttpSession
@SpringBootApplication
public class ShiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiroApplication.class, args);
	}

}
