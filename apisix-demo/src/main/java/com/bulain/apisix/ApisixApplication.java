package com.bulain.apisix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bulain.apisix", "org.apache.apisix.plugin.runner"})
public class ApisixApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApisixApplication.class, args);
    }

}