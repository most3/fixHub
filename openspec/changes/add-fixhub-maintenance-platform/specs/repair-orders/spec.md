## ADDED Requirements

### Requirement: 报修单提交
系统 SHALL 允许报修用户提交报修单，包含设备信息、故障描述与可选图片。

#### Scenario: 创建报修单
- **WHEN** 报修用户提交设备信息与故障描述
- **THEN** 系统应创建报修单并将状态设为待受理

### Requirement: 派单与接单
系统 SHALL 允许管理员派单给维修工，维修工接单后进入处理中。

#### Scenario: 派单
- **WHEN** 管理员将工单指派给维修工
- **THEN** 工单状态更新为处理中，并关联维修工

### Requirement: 处理与结果回填
系统 SHALL 允许维修工填写处理结果并提交。

#### Scenario: 提交处理结果
- **WHEN** 维修工提交处理结果
- **THEN** 工单状态更新为待评价，并保存处理结果

### Requirement: 确认与评价
系统 SHALL 允许报修用户确认并评价维修结果。

#### Scenario: 评价完成
- **WHEN** 报修用户提交评价
- **THEN** 工单状态更新为已完成并记录评分/评价内容

### Requirement: 状态跟踪
系统 SHALL 支持报修用户与管理员实时查看工单状态。

#### Scenario: 状态查询
- **WHEN** 用户查询工单详情
- **THEN** 系统返回当前状态与关键时间戳
