package com.bulain.mcp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class McpServerService {

    private final Map<String, String> code = new HashMap<>() {{
        put("北京", "110100");
        put("上海", "310100");
    }};

    @Tool(name = "weather", description = "获取城市天气信息")
    public String weather(@ToolParam(description = "城市") String city) {
        return String.format("您查询的城市为：%s", code.get(city));
    }

    @Tool(name = "goodsprice", description = "搜索商品价格")
    public String goodsprice(@ToolParam(description = "商品") String goods) {
        return "50元";
    }

}
