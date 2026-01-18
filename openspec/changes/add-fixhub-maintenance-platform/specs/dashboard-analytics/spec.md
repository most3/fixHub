## ADDED Requirements

### Requirement: 维修量统计
系统 SHALL 提供维修量统计（按时间区间、按状态分布）。

#### Scenario: 查询维修量
- **WHEN** 管理员请求统计数据
- **THEN** 系统返回时间区间内工单数量与状态分布

### Requirement: 平均响应时间
系统 SHALL 计算并展示平均响应时间（提交至派单/处理的时长）。

#### Scenario: 查询平均响应时间
- **WHEN** 管理员请求响应时间统计
- **THEN** 系统返回平均响应时长指标

### Requirement: 用户满意度
系统 SHALL 统计用户满意度（评分均值、评价数量）。

#### Scenario: 查询满意度
- **WHEN** 管理员请求满意度指标
- **THEN** 系统返回评分均值与评价数量
