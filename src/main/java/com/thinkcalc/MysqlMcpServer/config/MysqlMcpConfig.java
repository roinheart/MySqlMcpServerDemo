package com.thinkcalc.MysqlMcpServer.config;

import com.thinkcalc.MysqlMcpServer.service.McpServiceTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class MysqlMcpConfig {

    /**
     * 注册mcp工具
     */
    @Bean
    public List<ToolCallback> MysqlTools(McpServiceTools mcpServiceTools) {
        return new ArrayList<>(List.of(ToolCallbacks.from(mcpServiceTools)));
    }
}
