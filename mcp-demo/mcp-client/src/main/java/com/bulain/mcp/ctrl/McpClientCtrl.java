package com.bulain.mcp.ctrl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RequestMapping("/ai")
@Tag(name = "ai")
@RestController
public class McpClientCtrl {

    @Resource
    private ChatClient chatClient;

    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam("prompt") String userInput) {
        return chatClient.prompt(userInput).stream().content();
    }

}
