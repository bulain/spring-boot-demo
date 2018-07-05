package com.bulain.webflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bulain.webflux.hdl.TimeHandler;
import com.bulain.webflux.pojo.Event;

@Configuration
public class WebfluxConfig {
    @Autowired
    private TimeHandler timeHandler;

    @Bean
    public RouterFunction<ServerResponse> timerRouter() {
        return RouterFunctions
        		.route(RequestPredicates.GET("/time"), req -> timeHandler.getTime(req))
                .andRoute(RequestPredicates.GET("/date"), timeHandler::getDate)
                .andRoute(RequestPredicates.GET("/times"), timeHandler::sendTimePerSec);
    }
    
    @Bean 
    public CommandLineRunner initData(MongoOperations mongo) {
        return (String... args) -> {   
            mongo.dropCollection(Event.class);    
            mongo.createCollection(Event.class, CollectionOptions.empty().size(200).capped());   
        };
    }
    
}
