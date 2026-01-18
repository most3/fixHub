package com.fixhub.statistics.controller;

import com.fixhub.common.response.Result;
import com.fixhub.statistics.dto.DashboardStats;
import com.fixhub.statistics.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘统计接口，对外暴露统计数据查询。
 */
@RestController
@RequestMapping("/api/stats")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * 获取仪表盘所需的统计指标。
     */
    @GetMapping("/dashboard")
    public Result<DashboardStats> getDashboardStats() {
        return Result.success(statisticsService.getDashboardStats());
    }
}
