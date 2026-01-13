Admin seeder 工具

用途：在开发/初始化环境下，快速向 `fix_hub.users` 表插入一个管理员账号（密码使用 BCrypt 哈希）。

运行方式（在项目根目录执行）：

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="com.fixhub.tool.AdminSeeder" -Dexec.args="admin admin123"
```

参数说明（按顺序）：
1. username (可选，默认 `admin`)
2. password (可选，默认 `admin123`，至少 6 位)
3. displayName (可选，默认 `系统管理员`)
4. jdbcUrl (可选，默认指向本地 `fix_hub` 数据库)
5. dbUser (可选，默认 `root`)
6. dbPass (可选，默认 `123456`)

示例：指定数据库连接

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="com.fixhub.tool.AdminSeeder" -Dexec.args="admin StrongP@sswd 系统管理员 jdbc:mysql://127.0.0.1:3306/fix_hub?useSSL=false root 123456"
```

注意事项：
- 该工具仅用于开发或部署时初始化管理员账号。生产环境应通过受控迁移或部署脚本创建管理员账号，并确保数据库凭证与访问控制安全。 
- 若 `users` 表已有同名用户，工具会中止以避免覆盖。
- 运行前请确保 MySQL 服务正在运行且数据库 `fix_hub` 已存在，或先创建数据库和表。

安全建议：
- 生成后请尽快使用管理员账号登录并修改密码；不要将密码明文存入代码仓库或公开位置。
Admin seeder 工具

用途：在开发/初始化环境下，快速向 `fix_hub.users` 表插入一个管理员账号（密码使用 BCrypt 哈希）。

运行方式（在项目根目录执行）：

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="com.fixhub.tool.AdminSeeder" -Dexec.args="admin admin123"
```

参数说明（按顺序）：
1. username (可选，默认 `admin`)
2. password (可选，默认 `admin123`，至少 6 位)
3. displayName (可选，默认 `系统管理员`)
4. jdbcUrl (可选，默认指向本地 `fix_hub` 数据库)
5. dbUser (可选，默认 `root`)
6. dbPass (可选，默认 `123456`)

示例：指定数据库连接

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="com.fixhub.tool.AdminSeeder" -Dexec.args="admin StrongP@sswd 系统管理员 jdbc:mysql://127.0.0.1:3306/fix_hub?useSSL=false root 123456"
```

注意事项：
- 该工具仅用于开发或部署时初始化管理员账号。生产环境应通过受控迁移或部署脚本创建管理员账号，并确保数据库凭证与访问控制安全。 
- 若 `users` 表已有同名用户，工具会中止以避免覆盖。
- 运行前请确保 MySQL 服务正在运行且数据库 `fix_hub` 已存在，或先创建数据库和表。

安全建议：
- 生成后请尽快使用管理员账号登录并修改密码；不要将密码明文存入代码仓库或公开位置。