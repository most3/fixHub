# Spring Boot + Vue 通用交付规则（可复用模板）

## 1. 通用原则
- 遵循 SOLID / DRY / KISS / YAGNI；提前想清楚扩展点，避免过度设计。
- 遵循 OWASP Top 10：输入验证、鉴权授权、日志脱敏、敏感配置不入库。
- 采用分层架构（Controller → Service → Repository / Client），禁止跨层调用。
- 代码必须具备自动化测试（单测+接口/端到端），关键路径覆盖率 ≥ 80%。
- 所有新功能必须可观察（日志、指标、链路），默认开启健康检查。

## 2. 技术栈基线
- 后端：Spring Boot 3.x，Java 17/21，Spring Data JPA/MyBatis（按项目定），Spring Security（如需权限）。
- 文档：OpenAPI 3（springdoc-openapi），要求接口变更同步更新。
- 前端：Vue 3 + TypeScript + Vite；状态管理 Pinia，路由 Vue Router 4，HTTP 客户端 Axios。
- 代码质量：格式化（Spotless/Prettier），静态检查（Checkstyle/PMD/ESLint），提交前本地必须通过。

## 3. 架构与模块边界
- 模块划分：domain（实体/领域服务）、application（用例服务）、infrastructure（数据库/消息/外部接口）、interface（REST/任务）。
- DTO/VO 与实体分离，禁止直接向前端返回实体；对外只暴露 API 契约模型。
- 领域事件/集成事件区分清晰；跨边界使用消息或同步 API，避免共享数据库表。
- 配置与密钥通过外部化（环境变量/配置中心/Secret 管理），不同环境使用 Profile/环境变量切换。

## 4. 后端代码规范
- Controller：只做请求校验、装配和调用，返回统一响应模型；分页统一 page/size，排序字段白名单校验。
- Service：承载业务规则与事务，`@Transactional` 仅放在 Service；避免在循环内发起 DB/远程调用，优先批量。
- Repository：JPA 使用 `EntityGraph`/`fetch join` 防止 N+1；MyBatis SQL 必须参数绑定，禁止字符串拼接；索引设计随表DDL提交。
- 异常：业务异常使用自定义 `BizException`，在全局异常处理器里转换为标准响应；日志里记录错误码与请求标识。
- 日志：使用 SLF4J，禁止 System.out；敏感字段（密码、token、身份证等）脱敏；为关键链路添加 traceId。

## 5. API 设计与契约
- 风格：RESTful，资源名用复数名词，HTTP 方法语义正确；必要时为查询提供 `/search` 端点。
- 状态码：2xx 成功，4xx 客户端错误（含校验、鉴权失败），5xx 服务器错误；错误响应包含 code/message/requestId。
- 版本：路径或 Header 管理，如 `/api/v1`；破坏性变更必须提升版本并保持旧版灰度期。
- 校验：入参使用 JSR-380 注解 + `@Valid`；列表/批量输入需长度上限；文件上传限制大小与类型。

## 6. 数据与事务
- 表设计：必备主键、审计字段（created_at/updated_at/creator/updater）、逻辑删除字段（如需）；时间统一 UTC 存储，前端展示按时区转换。
- 事务：只在 Service；明确传播行为；跨服务事务优先使用补偿/幂等设计。
- 幂等：为写操作设计幂等键或使用去重表；重试需配合幂等校验。
- 性能：大表分页使用游标/搜索引擎；热点查询可加缓存并设置过期和穿透保护。

## 7. 安全与合规
- 鉴权：默认需要登录态，使用 JWT/OAuth2/Spring Security；接口按资源+操作粒度授权。
- 输入安全：禁止拼接 SQL/脚本；对富文本做 XSS 过滤；文件上传走白名单并存储在对象存储，不落本地临时目录。
- CORS：仅允许受信域名；OPTIONS 预检正确返回；敏感接口禁止跨域暴露。
- 审计：登录、权限变更、资金/关键业务操作记录审计日志；审计日志与业务日志分渠道。

## 8. 前端（Vue 3 + TS）规范
- 代码组织：`src` 下按 `modules`/`features` 分包；组件命名 `PascalCase`；组合式 API + `<script setup>`。
- 状态管理：Pinia store 按领域拆分，禁止全局巨型 store；异步请求封装成 service 层，组件不直接写 axios 细节。
- 接口约定：统一 axios 实例，默认超时与重试策略；拦截器添加 traceId/Authorization；错误统一 Toast/Message 处理。
- 路由与鉴权：路由 meta 配置权限点与标题；前置守卫校验登录与权限；敏感路由懒加载。
- UI/体验：表单必填校验、节流/防抖处理高频点击；长列表虚拟滚动；下载/导出提供进度提示。
- 工程：TypeScript 严格模式；ESLint + Prettier + Stylelint；单测（Vitest）覆盖关键逻辑，E2E（Cypress/Playwright）覆盖主流程。

## 9. 前后端协作规范
- API 定义：以 OpenAPI/Swagger 文档为单一真相，Mock 服务与前端联调共用；变更需评审并在 PR 描述中声明。
- 数据约定：分页返回统一 `page/size/total/records`；时间戳统一 ISO8601；布尔值不用 0/1 魔法数。
- 错误码：分模块分段，如 `A100x` 用户、`O200x` 订单；前端根据错误码做可恢复性提示。
- 文件与导出：统一流式下载接口和 Content-Type；前端按后端提供的文件名/MD5 校验。

## 10. 测试与质量门禁
- 后端：JUnit5 + MockMvc/RestAssured；Repository 层用 Testcontainers/H2；服务间调用用 WireMock/MockServer。
- 前端：Vitest 覆盖逻辑函数、Pinia store；Cypress/Playwright 覆盖登录、下单等主链路。
- 覆盖率：核心模块 ≥ 80%，低于阈值拒绝合并；禁止忽略测试的 `@Disabled` 长期存在。
- 质量检查：CI 执行格式化、lint、单测、构建；安全扫描（依赖漏洞、SAST）失败禁止发布。

## 11. 可观测性与运维
- 日志：按模块/traceId 打印；敏感数据脱敏；日志等级默认 info，异常 error，并包含请求上下文。
- 指标：暴露 JVM/应用指标（Micrometer + Prometheus）；关键业务指标（下单成功率、支付耗时）自定义埋点。
- 链路：接入 SkyWalking/Zipkin/Jaeger；跨服务调用传递 traceId/spanId。
- 健康检查：`/actuator/health` 必须可用，依赖探针细分；启动/存活/就绪探针区分。

## 12. CI/CD 与发布
- 分支：GitFlow 或 Trunk + feature 分支；PR 必须通过 CI 和 Code Review。
- 构建：后端使用 Maven/Gradle，前端使用 pnpm/npm；生成物带版本号与构建时间。
- 发布：灰度/金丝雀优先；支持回滚（版本化镜像 + 数据库变更回滚方案）。
- 资产：前端静态资源开启 gzip/br 压缩与缓存策略；后端容器镜像最小化并修剪层。

## 13. 配置与环境
- 配置分层：application.yml 只放默认配置；环境差异用 `application-*.yml` 或环境变量覆盖。
- 秘钥：使用环境变量/密钥管理服务（如 KMS/Secret）；绝不提交到 Git；提供示例 `.env.example`。
- 环境隔离：dev/stage/prod 数据与账号隔离；测试账户与真实用户隔离；重要操作需双人审核。

## 14. 数据迁移与脚本
- DDL/DML 通过版本化脚本（Flyway/Liquibase）；每次发布数据库变更与应用版本绑定。
- 脚本可回滚；大表变更提前评估锁表与耗时，必要时分批/影子表迁移。

## 15. 性能与容量
- 缓存：显式过期时间；防击穿/穿透/雪崩（布隆、互斥锁、随机过期）。
- 消息/异步：使用消息队列时确保幂等与死信处理；任务使用可重入 Job，避免并发重复执行。
- 前端性能：首屏体积控制（按需加载、分包）；图片懒加载与压缩；CDN 加速静态资源。

## 16. 文档与协作
- 代码内 Javadoc/注释保持最新；公共模块写简要 README（用途、接口、示例）。
- PR 模板包含：需求背景、变更点、测试结果、风险与回滚方案。
- 生产变更须有运行手册：启动命令、健康检查、回滚步骤、联系方式。

---
本规则作为下一个 Spring Boot + Vue 项目的通用基线，可在具体项目中按需裁剪与细化。优先保证安全、可观测、可回滚和可测试。
