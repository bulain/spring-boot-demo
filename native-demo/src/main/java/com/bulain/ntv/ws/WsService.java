package com.bulain.ntv.ws;

import java.util.List;

public interface WsService {

    List<String> ids();

    void send(String id, String text);

}
