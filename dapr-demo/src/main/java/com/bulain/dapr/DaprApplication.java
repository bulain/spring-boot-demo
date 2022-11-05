package com.bulain.dapr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DaprApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DaprApplication.class);
        app.run(args);
    }

}
