package com.bulain.mcp.ctrl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api")
@Tag(name = "api")
@RestController
public class McpClientCtrl {

    @Resource
    private ChatClient chatClient;

    @PostMapping("chat")
    public ResponseEntity<String> chat(@RequestBody String inputMessage) {
        try {
            String content = chatClient.prompt()
                    .user(inputMessage)
                    .call()
                    .content();
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.ok("处理请求时出错: " + e.getMessage());
        }
    }


}
