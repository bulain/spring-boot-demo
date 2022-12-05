package com.bulain.ntv.ws;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
public class WsServerConfig implements WebSocketConfigurer {

    @Resource
    private DefaultWsHandler defaultWsHandler;
    @Resource
    private DefaultWsInterceptor defaultWsInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(defaultWsHandler, "/wss")
                .addInterceptors(defaultWsInterceptor)
                .setAllowedOrigins("*");
    }
}
