package com.bulain.mcp.ctrl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RequestMapping("/api")
@Tag(name = "api")
@RestController
public class McpClientCtrl {

    @Resource
    private ChatClient chatClient;

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody String userInput) {
        return chatClient
                .prompt(userInput)
                .stream()
                .content();
    }

}
