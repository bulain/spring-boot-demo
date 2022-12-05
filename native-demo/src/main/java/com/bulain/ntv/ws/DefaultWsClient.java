package com.bulain.ntv.ws;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Slf4j
public class DefaultWsClient extends WebSocketClient {



    public DefaultWsClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("onOpen()");
    }

    @Override
    public void onMessage(String s) {
        log.info("onMessage() - {}", s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("onClose()");
    }

    @Override
    public void onError(Exception e) {
        log.info("onError()");
    }

}
