package com.fixhub.repair.repository;

import com.fixhub.repair.model.RepairOrder;
import com.fixhub.repair.model.RepairOrder.OrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {

    List<RepairOrder> findByReporterId(Long reporterId);

    List<RepairOrder> findByTechnicianId(Long technicianId);

    List<RepairOrder> findByStatus(OrderStatus status);
}
