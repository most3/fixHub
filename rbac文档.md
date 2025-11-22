# RBAC 文档

## 概要
本项目（sky-take-out）目前使用基于 JWT 的认证机制实现用户（前端微信用户）与管理员（员工）登录授权。代码库中未找到明确的角色（role）、权限（permission）、菜单（menu）等 RBAC 专用表或接口。下面列出当前与鉴权相关的接口、表设计（实际存在的实体/Mapper），并给出建议的 RBAC 扩展方案。

---

## 一、认证（Authentication）配置
配置文件：`sky-server/src/main/resources/application.yml`

主要配置（位于 `sky.jwt` 下）：
- admin-secret-key: itcast  （管理员端 JWT 签名密钥）
- admin-ttl: 72000000000   （管理员 token 过期时间，毫秒）
- admin-token-name: token  （管理员端从 header 中读取 token 的 header 名称）
- user-secret-key: itheima  （用户端 JWT 签名密钥）
- user-ttl: 7200000        （用户 token 过期时间，毫秒）
- user-token-name: authentication （用户端从 header 中读取 token 的 header 名称）

JWT 工具：`sky-common/src/main/java/com/sky/utils/JwtUtil.java`
拦截器：
- `JwtTokenAdminInterceptor`（拦截并校验 admin token，提取 EMP_ID 存入线程上下文 BaseContext）
- `JwtTokenUserInterceptor`（拦截并校验 user token，提取 USER_ID 存入线程上下文 BaseContext）

拦截器注册：`WebMvcConfiguration` 的 `addInterceptors` 已添加上述两个拦截器并排除了登录接口。

---

## 二、实际存在的与鉴权/用户相关的表（从实体/mapper/mapper.xml 提取）
下面列出的表是从实体类与 mapper/xml 推断而来，字段类型以实体或 mapper SQL 片段为准。

1) 表：`employee`（员工 / 管理员）
- 对应实体：`sky-pojo/src/main/java/com/sky/entity/Employee.java`
- Mapper：`sky-server/src/main/java/com/sky/mapper/EmployeeMapper.java`
- 主要字段：
  - id (BIGINT) 主键
  - username (VARCHAR) 登录用户名
  - name (VARCHAR) 员工姓名
  - password (VARCHAR) 密码（存储 MD5 或哈希值，代码中使用 MD5 默认密码）
  - phone (VARCHAR)
  - sex (VARCHAR)
  - id_number (VARCHAR)
  - status (INT) 帐号状态（启用/禁用）
  - create_time (DATETIME)
  - update_time (DATETIME)
  - create_user (BIGINT)
  - update_user (BIGINT)

典型 SQL 片段（来自 Mapper 注解）：
- Insert 示例：
  insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) values (...)

2) 表：`user`（微信前端用户）
- 对应实体：`sky-pojo/src/main/java/com/sky/entity/User.java`
- Mapper：`sky-server/src/main/java/com/sky/mapper/UserMapper.java` 与 `sky-server/src/main/resources/mapper/UserMapper.xml`
- 主要字段：
  - id (BIGINT) 主键
  - openid (VARCHAR) 微信 openid（唯一）
  - name (VARCHAR)
  - phone (VARCHAR)
  - sex (VARCHAR) （注：代码为 String）
  - id_number (VARCHAR)
  - avatar (VARCHAR)
  - create_time (DATETIME)

Insert SQL（来自 UserMapper.xml）：
- insert into user (openid, name, phone, sex, id_number, avatar, create_time) values (...)


---

## 三、与鉴权相关的接口（实际存在）
以下接口均来自代码中的 Controller 层，列出路径、HTTP 方法、参数、返回类型与简要说明。

1) 管理端（员工）
- POST /admin/employee/login
  - 描述：员工登录
  - 请求：EmployeeLoginDTO（{username, password}）
  - 返回：Result<EmployeeLoginVO>，包含 id、userName、name、token（JWT）
  - 权限：公开（在 WebMvcConfiguration 中被排除在拦截器之外）

- POST /admin/employee/logout
  - 描述：员工登出（实现为空，仅返回成功）
  - 权限：需要 admin JWT（拦截器会验证）

- POST /admin/employee
  - 描述：新增员工
  - 请求：EmployeeDTO
  - 返回：Result
  - 权限：需要 admin JWT

- GET /admin/employee/page
  - 描述：分页查询员工
  - 请求参数：EmployeePageQueryDTO（page, pageSize, name 等）
  - 返回：Result<PageResult>
  - 权限：需要 admin JWT

- POST /admin/employee/status/{status}
  - 描述：启用/禁用员工账号
  - 参数：status（路径）、id（查询或 body 中）
  - 返回：Result
  - 权限：需要 admin JWT

- GET /admin/employee/{id}
  - 描述：根据 id 查询员工信息
  - 返回：Result<Employee>
  - 权限：需要 admin JWT

- PUT /admin/employee
  - 描述：编辑员工信息
  - 请求：EmployeeDTO
  - 返回：Result
  - 权限：需要 admin JWT

2) 用户端（微信用户）
- POST /user/user/login
  - 描述：微信小程序登录
  - 请求：UserLoginDTO（包含 code、其它）
  - 返回：Result<UserLoginVO>，包含 id、openid、token（JWT）
  - 权限：公开

（项目里没有发现诸如 /admin/role 或 /admin/permission 的接口）


---

## 四、未找到但常见的 RBAC 组件（项目中缺失）
项目中没有发现下列常见 RBAC 组件的实现：
- role 表（角色表，例如 role(id, name, code, description, create_time)）
- permission 表（权限表，例如 permission(id, name, url, method, description, create_time)）
- role_permission 关联表（角色-权限关联）
- user_role 或 employee_role 关联表（用户/员工-角色关联）
- menu 表（菜单/路由列表）以及 menu_role 或 role_menu 的关联
- 基于注解/注解解析的权限控制（如 @RequiresPermissions），或基于数据库的权限缓存/动态加载


---

## 五、建议（如果要补全 RBAC）
下面给出基于现有结构的可行扩展建议，包含表设计与必要接口，便于快速集成 RBAC。

A. 数据库表设计建议
1. role
- id BIGINT PK
- name VARCHAR(64) 角色显示名
- code VARCHAR(64) 角色编码（唯一，如 ADMIN、OPERATOR）
- description VARCHAR(255)
- create_time DATETIME

2. permission
- id BIGINT PK
- name VARCHAR(128)
- code VARCHAR(128)
- url VARCHAR(255)    // 如 /admin/employee
- method VARCHAR(10) // GET, POST
- type INT           // 1=菜单 2=接口
- create_time DATETIME

3. role_permission
- role_id BIGINT
- permission_id BIGINT
- 主键( role_id, permission_id )

4. employee_role（或者 user_role）
- employee_id BIGINT
- role_id BIGINT
- 主键( employee_id, role_id )

5. menu（可选）
- id, name, path, parent_id, order, icon 等字段

B. 接口建议（示例）
- GET /admin/roles — 列表角色
- POST /admin/roles — 新建角色
- PUT /admin/roles/{id} — 更新
- DELETE /admin/roles/{id}
- GET /admin/permissions — 列表权限
- POST /admin/roles/{roleId}/permissions — 给角色分配权限（传 permissionId 列表）
- POST /admin/employees/{empId}/roles — 给员工分配角色

C. 运行时权限校验建议
- 在 JwtTokenAdminInterceptor 中解析 empId 后，加载该员工所拥有的权限集合（从缓存或 DB），并把权限放入请求上下文
- 在 Controller/Handler 层，使用自定义注解（如 @RequirePermission(code="employee:save")）或基于路径+方法的校验器去校验当前请求是否包含对应权限


---

## 六、交付与下一步
1. 本文档为初步 RBAC 探索报告，基于代码现有实现（主要是 JWT 认证与员工/用户表）。
2. 如果需要我可以：
   - 生成完整的数据库建表 SQL
   - 在代码中新增 Role/Permission 实体、Mapper、Controller、Service，实现基础 CRUD 与分配接口
   - 集成注解/拦截器型的权限校验（以及缓存）


---

文档生成者：自动化代码助手（基于项目源码检查）
时间：2025-10-09
