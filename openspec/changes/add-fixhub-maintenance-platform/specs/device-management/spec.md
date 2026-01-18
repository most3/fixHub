## ADDED Requirements

### Requirement: 设备维护
系统 SHALL 允许管理员维护学校公共设备信息（新增、编辑、停用）。

#### Scenario: 新增设备
- **WHEN** 管理员提交设备信息
- **THEN** 系统应创建设备记录并可用于报修关联

### Requirement: 设备查询
系统 SHALL 允许报修用户查看设备列表以便报修选择。

#### Scenario: 查看设备列表
- **WHEN** 报修用户访问设备列表
- **THEN** 系统返回可用设备信息
