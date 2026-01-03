package com.fixhub.repair.mapper;

import com.fixhub.repair.model.RepairOrder;
import java.time.Instant;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 维修工单持久化 Mapper。
 */
@Mapper
public interface RepairOrderMapper {

    RepairOrder selectById(@Param("id") Long id);

    List<RepairOrder> selectAll();

    List<RepairOrder> selectByReporterId(@Param("reporterId") Long reporterId);

    List<RepairOrder> selectByTechnicianId(@Param("technicianId") Long technicianId);

    int insert(RepairOrder order);

    int updateAssignment(@Param("orderId") Long orderId,
                         @Param("technicianId") Long technicianId,
                         @Param("assignedAt") Instant assignedAt,
                         @Param("status") String status);

    int updateCompletion(@Param("orderId") Long orderId,
                         @Param("resultDesc") String resultDesc,
                         @Param("repairedAt") Instant repairedAt,
                         @Param("status") String status);

    int updateClosure(@Param("orderId") Long orderId,
                      @Param("closedAt") Instant closedAt,
                      @Param("status") String status);
}
