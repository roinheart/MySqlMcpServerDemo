package com.thinkcalc.MysqlMcpServer.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkcalc.MysqlMcpServer.utils.DBUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            @ToolParam(description = "参数格式：application/json;表名:tableName,列名:columnNames用逗号隔开,待插入数据Json数组:values") JSONObject paramData) {
        String tableName = paramData.getString("tableName");
        String columnNames = paramData.getString("columnNames").replace(" ", "");
        JSONArray values = JSONArray.parseArray(paramData.getJSONArray("values").toJSONString());
        Integer integer = DBUtils.insert(tableName, columnNames, values);
        return "数据插入成功" + integer + "条！";
    }

    @Tool(name = "insertBySql", description = "通过SQL语句插入数据")
    public String insertBySql(@ToolParam(description = "待执行的INSERT SQL语句") String sql) {
        Integer integer = DBUtils.insertOrUpdateBySql(sql);
        return "数据插入成功" + integer + "条！";
    }

}
