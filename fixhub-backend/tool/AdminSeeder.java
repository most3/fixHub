package com.fixhub.tool;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 简单的管理员种子工具：将一个管理员账号插入到数据库 `users` 表中。
 * 使用方式（在项目根目录执行）：
 * mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="com.fixhub.tool.AdminSeeder" -Dexec.args="admin admin123"
 * 可选参数： username password displayName jdbcUrl dbUser dbPass
 * 默认 jdbcUrl 和 db 凭证与 application.yml 对应。
 *
 * 注意：该工具仅用于开发/初始化环境，在生产环境请通过更安全的迁移/部署流程创建管理员。
 */
public class AdminSeeder {

    public static void main(String[] args) {
        String username = args.length > 0 ? args[0] : "admin";
        String password = args.length > 1 ? args[1] : "admin123";
        String displayName = args.length > 2 ? args[2] : "系统管理员";
        String jdbcUrl = args.length > 3 ? args[3] : "jdbc:mysql://localhost:3306/fix_hub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
        String dbUser = args.length > 4 ? args[4] : "root";
        String dbPass = args.length > 5 ? args[5] : "123456";

        System.out.println("准备在数据库中插入管理员账号: " + username);
        System.out.println("JDBC: " + jdbcUrl + " user=" + dbUser);

        if (password.length() < 6) {
            System.err.println("错误：密码长度至少6位，请使用更安全的密码");
            System.exit(1);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode(password);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("找不到 MySQL JDBC 驱动，请确保已在 pom.xml 中添加依赖并已下载依赖");
            e.printStackTrace();
            System.exit(2);
        }

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)) {
            // 检查 users 表是否存在并是否已有同名用户
            PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE username = ? LIMIT 1");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                System.out.println("用户已存在：" + username + "，操作已中止。如需覆盖请手动删除或更新该记录。");
                return;
            }

            PreparedStatement ins = conn.prepareStatement("INSERT INTO users (username, password_hash, display_name, role, phone) VALUES (?, ?, ?, ?, ?)");
            ins.setString(1, username);
            ins.setString(2, hashed);
            ins.setString(3, displayName);
            ins.setString(4, "ADMIN");
            ins.setString(5, "");

            int n = ins.executeUpdate();
            if (n > 0) {
                System.out.println("管理员创建成功：" + username);
                System.out.println("请尽快用该账号登录并修改密码。");
            } else {
                System.err.println("插入失败。请检查数据库连接与表结构。");
            }
        } catch (SQLException e) {
            System.err.println("数据库操作异常：" + e.getMessage());
            e.printStackTrace();
            System.exit(3);
        }
    }
}
