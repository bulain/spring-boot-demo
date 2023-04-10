package com.bulain.ntv.ws;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DefaultWsHandler implements WebSocketHandler, WsService {

    private Map<String, WebSocketSession> mapSession = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished()");
        mapSession.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("handleMessage()");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("handleTransportError()");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("afterConnectionClosed()");
        mapSession.remove(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public List<String> ids() {
        return new ArrayList<>(mapSession.keySet());
    }

    @SneakyThrows
    @Override
    public void send(String id, String text) {
        WebSocketSession session = mapSession.get(id);
        if (session != null) {
            TextMessage message = new TextMessage(text);
            session.sendMessage(message);
        }
    }

}
