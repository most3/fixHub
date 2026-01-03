package com.fixhub.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 仪表盘展示的核心统计指标。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {

    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
    private double averageRating;
}
