# 使用Spring AI开发的MySQL MCP服务器 - MysqlMcpServer

## 项目简介

### 基于Spring AI框架的MySQL MCP服务器,可用于cline，通义灵码等工具。

### 安装与配置步骤

1. 获取项目代码

```shell
   git clone https://github.com/roinheart/MySqlMcpServerDemo.git
```

2. 配置数据库连接

```yaml
spring:
   application:
      name: Mysql-Mcp-Server
   datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://127.0.0.1:3306/you_db?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
      username: root
      password: root
   ai:
      mcp:
         server:
            stdio: true
   main:
      banner-mode: off
      web-application-type: none

logging:
   pattern:
      console:

```

3. 构建项目

```shell
   mvn install
```

4. 配置mcpServers

```json
{
  "mcpServers": {
    "mysql-mcp": {
      "autoApprove": [],
      "disabled": false,
      "timeout": 600,
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-Dspring.main.web-application-type=none",
        "-Dlogging.pattern.console=",
        "-jar",
        "D:\\Code\\MysqlMcpServer\\target\\Mysql-Mcp-Server-0.0.1-SNAPSHOT.jar"
      ],
      "env": {},
      "transportType": "stdio"
    }
  }
}
```

### 注意事项

1. 请将jar路径D:/code/Mysql-Mcp-Server/target/...替换为您实际的jar文件路径
2. 确保您的系统已安装Java运行环境
3. 根据您的MySQL服务器配置正确设置连接参数

### 使用说明

配置完成后，该服务将通过标准输入输出与主程序通信，提供MySQL查询和数据插入功能。

### 功能列表

1. **MySQL查询功能**
   - 工具名称: `queryMysql`
   - 描述: 执行标准SQL查询语句
   - 参数: SQL查询语句

2. **MySQL数据批量插入功能**
   - 工具名称: `insertMysql`
   - 描述: 批量插入数据到指定表
   - 参数格式: JSON对象，包含以下字段:
     - `tableName`: 表名
     - `columnNames`: 列名，用逗号分隔
     - `values`: 待插入的数据数组，每个元素是一个包含列值的JSON对象

   示例:
   ```json
   {
     "tableName": "your_table",
     "columnNames": "id,name,description",
     "values": [
       {"id": 1, "name": "测试1", "description": "描述1"},
       {"id": 2, "name": "测试2", "description": "描述2"}
     ]
   }
   ```

3. **通过SQL语句插入数据功能**
   - 工具名称: `insertBySql`
   - 描述: 通过SQL语句直接插入数据
   - 参数: INSERT SQL语句

   示例:
   ```sql
   INSERT INTO your_table (id, name, description) VALUES (3, '测试3', '描述3')
   ```