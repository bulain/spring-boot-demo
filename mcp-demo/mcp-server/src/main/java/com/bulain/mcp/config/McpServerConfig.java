package com.bulain.mcp.config;

import com.bulain.mcp.service.McpServerService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider weatherTools(McpServerService mcpServerService) {
        return MethodToolCallbackProvider.builder().toolObjects(mcpServerService).build();
    }

}
