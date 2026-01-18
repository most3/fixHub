## ADDED Requirements

### Requirement: 角色模型
系统 SHALL 支持三类角色：报修用户（REPORTER）、维修工（TECHNICIAN）、管理员（ADMIN）。

#### Scenario: 角色识别
- **WHEN** 用户登录系统
- **THEN** 系统应在会话上下文中识别并暴露其角色

### Requirement: 管理员生成维修工账号
系统 SHALL 允许管理员生成随机维修工账号，并可下发给维修工使用。

#### Scenario: 生成账号
- **WHEN** 管理员请求生成维修工账号
- **THEN** 系统应创建随机用户名与初始密码并返回给管理员

### Requirement: 维修工资料补全
系统 SHALL 要求维修工首次登录后补全手机号等基础信息，未补全前不得接单。

#### Scenario: 首次登录校验
- **WHEN** 维修工首次登录
- **THEN** 系统应要求补全资料并阻止接单操作

### Requirement: 权限控制
系统 SHALL 根据角色限制关键操作（派单、设备维护、统计查看等）。

#### Scenario: 访问控制
- **WHEN** 报修用户尝试访问管理员接口
- **THEN** 系统应拒绝访问并返回权限不足错误
