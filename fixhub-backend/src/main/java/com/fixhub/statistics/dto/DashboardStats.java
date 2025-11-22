package com.fixhub.statistics.dto;

public class DashboardStats {

    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
    private double averageRating;

    public DashboardStats(long totalOrders, long pendingOrders, long completedOrders, double averageRating) {
        this.totalOrders = totalOrders;
        this.pendingOrders = pendingOrders;
        this.completedOrders = completedOrders;
        this.averageRating = averageRating;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public double getAverageRating() {
        return averageRating;
    }
}
