package com.bulain.mcp.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider tools) {
        return builder
                .defaultSystem("""
                你是一个AI助手，请分析用户的问题，做出不同的处理，你现在拥有以下能力
                1.如果用户的提问包含城市和天气，就为用户查询指定城市的天气
                2.如果用户的提问包含商品，就为用户查询商品信息
                """)
                .defaultToolCallbacks(tools)
                .build();
    }

}
