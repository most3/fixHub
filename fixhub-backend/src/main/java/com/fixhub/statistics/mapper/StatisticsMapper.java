package com.fixhub.statistics.mapper;

import com.fixhub.statistics.dto.DashboardStats;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis 映射接口，用于聚合统计仪表盘数据。
 */
@Mapper
public interface StatisticsMapper {

    DashboardStats selectDashboardStats();
}
