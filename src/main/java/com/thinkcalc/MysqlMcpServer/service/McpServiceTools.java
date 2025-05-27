package com.thinkcalc.MysqlMcpServer.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkcalc.MysqlMcpServer.utils.DBUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class McpServiceTools {

    @Autowired
    private DBUtils DBUtils;

    @Tool(name = "query", description = "mysql查询")
    public String query(@ToolParam(description = "输入的sql查询语句") String sql) {
        System.out.println("接收到的 SQL 查询: " + sql);
        return DBUtils.query(sql);
    }

    @Tool(name = "insert", description = "mysql数据批量插入")
    public String insert(
            @ToolParam(description = "Insert sql string") String sql) throws SQLException {
        return DBUtils.insert(sql) ? "数据插入成功" : "数据插入失败";
    }
}
