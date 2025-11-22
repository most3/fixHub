package com.fixhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用入口类 — FixHub 后端服务
 *
 * <p>负责启动 Spring Boot 应用。后续请在此类或 `application.yml` 中配置启动参数与基本环境信息。</p>
 */
@SpringBootApplication
public class FixHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(FixHubApplication.class, args);
    }
}
