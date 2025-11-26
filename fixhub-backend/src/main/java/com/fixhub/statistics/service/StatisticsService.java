package com.fixhub.statistics.service;

import com.fixhub.repair.model.Comment;
import com.fixhub.repair.model.RepairOrder;
import com.fixhub.repair.model.RepairOrder.OrderStatus;
import com.fixhub.repair.repository.CommentRepository;
import com.fixhub.repair.repository.RepairOrderRepository;
import com.fixhub.statistics.dto.DashboardStats;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 统计服务，用于汇总仪表盘展示所需的基础指标。
 */
@Service
public class StatisticsService {

    private final RepairOrderRepository orderRepository;
    private final CommentRepository commentRepository;

    public StatisticsService(RepairOrderRepository orderRepository, CommentRepository commentRepository) {
        this.orderRepository = orderRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * 汇总工单数量及满意度评分平均值。
     */
    public DashboardStats getDashboardStats() {
        List<RepairOrder> allOrders = orderRepository.findAll();
        long total = allOrders.size();
        long pending = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        long completed = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CLOSED).count();

        List<Comment> comments = commentRepository.findAll();
        double avgRating = comments.stream()
                .mapToInt(Comment::getRating)
                .average()
                .orElse(0.0);

        return new DashboardStats(total, pending, completed, avgRating);
    }
}
