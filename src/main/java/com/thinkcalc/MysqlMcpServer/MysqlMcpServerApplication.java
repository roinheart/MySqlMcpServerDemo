package com.thinkcalc.MysqlMcpServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MysqlMcpServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MysqlMcpServerApplication.class, args);
        System.out.println("Started MysqlMcpServerApplication");
    }
}
