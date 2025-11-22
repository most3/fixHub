# FixHub 后端服务

基于 Spring Boot 的 FixHub 设备报修与维护平台后端服务。本项目实现了用户注册与登录等基础功能，使用 MySQL 存储用户与数据。

## 运行前准备

- Java 17 及以上
- Maven 3.9 及以上
- 一台 MySQL 实例，数据库名为 `fix_hub`

在 MySQL 中建库（示例）：

```sql
CREATE DATABASE IF NOT EXISTS fix_hub DEFAULT CHARACTER SET utf8mb4;
```

## 配置说明

默认配置位于 `src/main/resources/application.yml`，默认连接示例：

- URL: `jdbc:mysql://localhost:3306/fix_hub`
- 用户名: `root`
- 密码: `123456`

如需在不同环境运行，请通过环境变量或配置文件替换这些值以保证安全性。

## 本地运行

启动项目（示例）：

```powershell
cd fixhub-backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev -DskipTests
```

接口说明：

- `POST /api/auth/register` – 注册用户
- `POST /api/auth/login` – 用户登录，返回登录信息

示例请求：

注册请求：

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "teacher01",
  "password": "secret123",
  "displayName": "物理老师",
  "role": "REPORTER"
}
```

登录请求：

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "teacher01",
  "password": "secret123"
}
```

## 测试

单元测试使用内存中的 H2 数据库运行：

```powershell
mvn test
```


