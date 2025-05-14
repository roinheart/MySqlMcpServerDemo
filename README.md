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
      "mysql": {
         "command": "java",
         "args": [
            "-Dspring.ai.mcp.server.stdio=true",
            "-Dspring.main.web-application-type=none",
            "-Dlogging.pattern.console=",
            "-jar",
            "/you/path/Mysql-Mcp-Server-0.0.1-SNAPSHOT.jar"
         ]
      }
   }
}
```
# 注意
1. 配置文件路径为：/you/path/Mysql-Mcp-Server-0.0.1-SNAPSHOT.jar，请自行修改。
2. 系统已安装JDK17及以上版本