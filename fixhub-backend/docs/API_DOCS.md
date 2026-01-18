# FixHub API 文档说明

- 文档入口（Swagger UI）：
  - http://localhost:8080/swagger-ui/index.html
  - 如果使用默认 Spring Boot 端口且在本地运行。

- OpenAPI 文档原始 JSON：
  - http://localhost:8080/v3/api-docs

- 说明：
  - 我们已使用 `springdoc-openapi` 集成接口文档，并在 `com.fixhub.config.OpenApiConfig` 中设置了中文的接口描述。
  - Swagger UI 的标题/接口说明会显示在页面上；已尽量将接口说明文字写为中文。
  - 若需要将 Swagger UI 的整体界面（按钮、提示等）完整本地化为中文，需替换或定制 Swagger UI 静态页面（这是可选项，需额外确认）。

- 启动并查看文档（在项目根目录 `fixhub-backend` 下执行）：

```powershell
# 使用 Maven 构建并运行
mvn spring-boot:run

# 或先构建再运行
mvn -DskipTests package
java -jar target/fixhub-backend-0.0.1-SNAPSHOT.jar
```

- 前端适配说明：
  - 前端 `fixHub-ui` 已调整 Axios 拦截器以兼容后端统一响应 `Result<T>`。前端请求成功时会直接返回 data 载荷。
  - 当后端返回 business 错误（非 200 code），前端会抛出异常并显示友好错误提示。

- 下一步建议：
  - 如果你希望 Swagger UI 按钮/提示完全中文化，我可以：
    1. 添加自定义 `swagger-ui` 静态页面并注入 `lang` 为 `zh-CN` 的配置；或
    2. 使用本地化的 Swagger UI 发行版替换默认静态资源（需增加前端静态文件）。

  ## 中文引导页面

  已添加一个简洁的中文引导页面，方便非技术人员直接查看与引导：

  - 页面地址（浏览器打开）： http://localhost:8080/api-docs.html
  - 页面会在内部嵌入 Swagger UI（如遇展示问题，可点击页面内的“打开 Swagger UI”按钮在新标签页中单独打开）。

  该引导页提供中文说明并直接将 Swagger UI 嵌入，便于快速阅读接口说明与示例。

