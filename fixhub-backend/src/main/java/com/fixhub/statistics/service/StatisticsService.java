package com.fixhub.statistics.service;

import cn.hutool.core.util.NumberUtil;
import com.fixhub.statistics.dto.DashboardStats;
import com.fixhub.statistics.mapper.StatisticsMapper;
import org.springframework.stereotype.Service;

/**
 * 统计服务，用于汇总仪表盘展示所需的基础指标。
 */
@Service
public class StatisticsService {

    private final StatisticsMapper statisticsMapper;

    public StatisticsService(StatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    /**
     * 汇总工单数量及满意度评分平均值。
     */
    public DashboardStats getDashboardStats() {
        DashboardStats stats = statisticsMapper.selectDashboardStats();
        if (stats == null) {
            stats = new DashboardStats(0, 0, 0, 0.0);
        }
        stats.setAverageRating(NumberUtil.round(stats.getAverageRating(), 2).doubleValue());
        return stats;
    }
}
