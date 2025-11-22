-- FixHub 数据库初始化脚本
-- 包含用户、设备、报修单、评价等核心表结构

CREATE DATABASE IF NOT EXISTS fix_hub DEFAULT CHARACTER SET utf8mb4;
USE fix_hub;

-- 1. 用户表 (已存在，此处仅为完整性展示，若已建表可忽略)
-- 角色：REPORTER(报修用户), TECHNICIAN(维修工), ADMIN(管理员)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    display_name VARCHAR(100) COMMENT '显示名',
    role VARCHAR(20) NOT NULL COMMENT '角色',
    phone VARCHAR(20) COMMENT '联系电话',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '用户表';

-- 2. 设备表 (Device)
-- 管理员维护的公共设备信息
CREATE TABLE IF NOT EXISTS devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '设备ID',
    name VARCHAR(100) NOT NULL COMMENT '设备名称',
    model VARCHAR(100) COMMENT '设备型号',
    location VARCHAR(100) NOT NULL COMMENT '所在位置',
    status VARCHAR(20) DEFAULT 'NORMAL' COMMENT '设备状态: NORMAL(正常), BROKEN(故障), SCRAPPED(报废)',
    purchase_date DATE COMMENT '采购日期',
    description TEXT COMMENT '设备描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '设备信息表';

-- 3. 报修单表 (RepairOrder)
-- 核心业务表，记录报修全流程
CREATE TABLE IF NOT EXISTS repair_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '报修单ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '报修单号(业务唯一标识)',
    
    reporter_id BIGINT NOT NULL COMMENT '报修人ID',
    device_id BIGINT COMMENT '关联设备ID(可选，若针对特定设备)',
    device_name VARCHAR(100) COMMENT '设备名称(若未关联设备表，可直接填名)',
    location VARCHAR(100) NOT NULL COMMENT '故障位置',
    
    description TEXT NOT NULL COMMENT '故障描述',
    image_url VARCHAR(255) COMMENT '故障图片URL',
    
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' 
        COMMENT '状态: PENDING(待受理), ASSIGNED(已派单/处理中), REPAIRED(维修完成/待确认), CLOSED(已关闭/已评价)',
    
    technician_id BIGINT COMMENT '维修工ID',
    
    result_desc TEXT COMMENT '维修处理结果描述',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
    assigned_at TIMESTAMP COMMENT '派单时间',
    repaired_at TIMESTAMP COMMENT '维修完成时间',
    closed_at TIMESTAMP COMMENT '关闭/评价时间',
    
    FOREIGN KEY (reporter_id) REFERENCES users(id),
    FOREIGN KEY (technician_id) REFERENCES users(id),
    FOREIGN KEY (device_id) REFERENCES devices(id)
) COMMENT '报修单表';

-- 4. 评价表 (Comment)
-- 用户对维修服务的评价
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评价ID',
    order_id BIGINT NOT NULL UNIQUE COMMENT '关联报修单ID',
    user_id BIGINT NOT NULL COMMENT '评价人ID',
    rating INT NOT NULL COMMENT '评分(1-5)',
    content TEXT COMMENT '评价内容',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
    
    FOREIGN KEY (order_id) REFERENCES repair_orders(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT '评价表';

-- 初始数据示例 (可选)
-- INSERT INTO users (username, password_hash, display_name, role) VALUES 
-- ('admin', '$2a$10$xE...', '系统管理员', 'ADMIN'),
-- ('tech01', '$2a$10$xE...', '张师傅', 'TECHNICIAN'),
-- ('student01', '$2a$10$xE...', '李同学', 'REPORTER');
