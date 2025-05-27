package com.thinkcalc.MysqlMcpServer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class McpServiceToolsTest {

//    @Autowired
//    private  McpServiceTools mcpServiceTools;
//    private static final String TEST_INSERT_SQL = "INSERT INTO `thinkcalc`.`tc_menu` (`id`, `pid`, `type`, `name`, `uri`, `page`, `icon`, `order`, `create_by`, `update_by`) VALUES (1900911956718653511, 1895023248077361200, 'page', '角色列表', '/role/list', NULL, NULL, 0, NULL, NULL);";
//    @Test
//    public void testInsert() throws Exception {
//        String result = mcpServiceTools.insert(TEST_INSERT_SQL);
//        assertTrue(result.contains("成功"), "数据插入应该成功");
//    }
    //这个测试方法有问题，不要使用
    public static void main(String[] args) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-Dspring.ai.mcp.server.stdio=true",
                "-Dspring.main.web-application-type=none",
                "-Dlogging.pattern.console=",
                "-jar",
                "target/Mysql-Mcp-Server-0.0.1-SNAPSHOT.jar"
        );
        pb.redirectErrorStream(true);
        Process proc = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8));
        OutputStreamWriter writer = new OutputStreamWriter(proc.getOutputStream(), StandardCharsets.UTF_8);

        // 1. 先读取并丢弃启动 banner，直到检测到服务端准备好
        String line;
        boolean ready = false;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            // Spring Boot 启动完成后通常会输出 "Started" 或 "Started XxxApplication"
            if (line.contains("Started")) {
                ready = true;
                break;
            }
        }

        if (!ready) {
            System.out.println("服务端未能正常启动！");
            proc.destroy();
            return;
        }

        // 2. 发送 query 请求
        String request = "{\"tool\":\"query\",\"args\":[\"SELECT * FROM tc_user\"]}\n";
        System.out.println("发送的请求：" + request);
        writer.write(request);
        writer.flush();
        System.out.println("请求已发送，等待响应...");

        // 3. 读取业务响应
        System.out.println("服务端返回：");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            // 你可以根据实际协议判断何时结束
            if (line.contains("]") || line.contains("}")) {
                break;
            }
        }

        writer.close();
        reader.close();
        proc.destroy();
    }
} 