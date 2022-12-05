package com.bulain.ntv.ws;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Slf4j
@Component
public class WsClientConfig {
    private Timer timer = new Timer();

    @Bean
    public WebSocketClient webSocketClient() {
        String ws = "ws://127.0.0.1:8080/wss";
        try {
            WebSocketClient webSocketClient = new DefaultWsClient(new URI(ws));
            webSocketClient.connect();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    log.info("检查连接状态...");
                    if (webSocketClient.isClosed()) {
                        log.info("断线重连....");
                        webSocketClient.reconnect();
                    }
                }
            }, 1000, 5000);
            return webSocketClient;
        } catch (URISyntaxException e) {
            log.error("webSocketClient()", e);
        }
        return null;
    }

}
