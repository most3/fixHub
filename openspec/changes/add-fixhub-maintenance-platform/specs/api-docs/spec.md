## ADDED Requirements

### Requirement: OpenAPI 文档
系统 SHALL 提供 OpenAPI 3 文档与 Swagger UI 入口。

#### Scenario: 查看文档
- **WHEN** 用户访问文档入口
- **THEN** 系统展示 Swagger UI 与接口列表

### Requirement: 中文提示与描述
系统 SHALL 在文档中提供中文接口描述与字段说明。

#### Scenario: 阅读接口
- **WHEN** 用户查看接口定义
- **THEN** 文档显示中文说明与示例提示
