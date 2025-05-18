package com.bulain.mcp.server;

import com.bulain.mcp.MpcServerApplication;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Map;

@Slf4j
@Disabled
@SpringBootTest(classes = MpcServerApplication.class)
class McpServerTest {

    @Test
    void sse() {
        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder("http://localhost:8081").build();
        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).build();
        mcpSyncClient.initialize();

        McpSchema.CallToolResult callToolResult = mcpSyncClient.callTool(new McpSchema.CallToolRequest("query_asn", Map.of("arg0", "AN20250518001")));
        log.info("{}", callToolResult);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

    @Test
    void stdio() {

        log.info("{}", (new File(".").getAbsolutePath()));

        ServerParameters stdioParams = ServerParameters.builder("java")
                .args("-Dspring.ai.mcp.server.stdio=true",
                        "-Dspring.main.web-application-type=none",
                        "-Dfile.encoding=UTF-8",
                        "-Dlogging.pattern.console=", "-jar",
                        "target/mcp-server-1.0.0-SNAPSHOT.jar")
                .build();

        StdioClientTransport stdioClientTransport = new StdioClientTransport(stdioParams);
        McpSyncClient mcpSyncClient = McpClient.sync(stdioClientTransport).build();
        mcpSyncClient.initialize();

        McpSchema.CallToolResult callToolResult = mcpSyncClient.callTool(new McpSchema.CallToolRequest("query_asn", Map.of("arg0", "AN20250518001")));
        log.info("{}", callToolResult);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

}

