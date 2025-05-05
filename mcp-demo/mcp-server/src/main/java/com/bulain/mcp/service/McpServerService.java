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

    @Tool(name = "queryWeather", description = "获取城市天气信息")
    public String queryWeather(@ToolParam(description = "城市") String city) {
        return String.format("%s，今日中雨，白天最高气温24℃，夜间最低温度17℃，东南风3级，空气质量优。", city);
    }

    @Tool(name = "queryGoodsprice", description = "搜索商品价格")
    public String queryGoodsprice(@ToolParam(description = "商品") String goods) {
        return "50元";
    }

}
