package com.bulain.xxljob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class XxljobHandler {
    
    public String handle(String params){
        log.info("handle() - {}", params);
        return "ret: " + params;
    }
    
}
