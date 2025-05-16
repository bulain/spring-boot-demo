package com.bulain.mcp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class McpQueryService {

    private final JdbcTemplate jdbcTemplate;

    @Tool(description = "查询数据库中所有的表")
    public List<Map<String, Object>> queryAllTables() {
        String sql = "select table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = 'public'";
        return jdbcTemplate.queryForList(sql);
    }

    @Tool(description = "查询数据库中的表的数据")
    public List<Map<String, Object>> queryTable(@ToolParam(description = "表名") String tableName) {
        String sql = "select * from " + tableName;
        return jdbcTemplate.queryForList(sql);
    }

}