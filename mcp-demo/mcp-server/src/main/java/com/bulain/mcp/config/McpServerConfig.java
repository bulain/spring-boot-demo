package com.bulain.mcp.config;

import com.bulain.mcp.service.McpQueryService;
import com.bulain.mcp.service.McpServerService;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider callbackProvider(McpServerService mcpServerService,
                                                 McpQueryService mcpQueryService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(mcpServerService)
                .build();
    }

    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> promptSpecList() {
        return List.of(new McpServerFeatures.SyncPromptSpecification(
                new McpSchema.Prompt("greeting", "description",
                        List.of(new McpSchema.PromptArgument("name", "description", true))),
                (exchange, request) -> {
                    McpSchema.PromptMessage message = new McpSchema.PromptMessage(
                            McpSchema.Role.USER, new McpSchema.TextContent("提示内容"));
                    return new McpSchema.GetPromptResult("description", List.of(message));
                }
        ));
    }

    @Bean
    public List<McpServerFeatures.SyncResourceSpecification> resourceSpecList() {
        return List.of(new McpServerFeatures.SyncResourceSpecification(
                new McpSchema.Resource("mysql://table1/meta", "table1", "meta data", "text/plain", null),
                (exchange, request) -> {
                    McpSchema.ResourceContents contents = new McpSchema.TextResourceContents(
                            "meta1", "text/plain", "资源内容");
                    return new McpSchema.ReadResourceResult(List.of(contents));
                }
        ));
    }

}
