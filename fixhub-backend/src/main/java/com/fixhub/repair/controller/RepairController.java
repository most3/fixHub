package com.fixhub.repair.controller;

import com.fixhub.repair.dto.CreateCommentRequest;
import com.fixhub.repair.dto.CreateOrderRequest;
import com.fixhub.repair.model.Comment;
import com.fixhub.repair.model.RepairOrder;
import com.fixhub.repair.service.RepairService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报修相关接口，负责工单创建、指派、完成、评价等操作。
 */
@RestController
@RequestMapping("/api/repairs")
public class RepairController {

    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    /**
     * 报修用户提交工单。
     */
    @PostMapping
    public ResponseEntity<RepairOrder> createOrder(@RequestParam Long userId,
                                                   @Valid @RequestBody CreateOrderRequest request) {
        // TODO: 在接入认证体系后，应从 SecurityContext 中获取 userId
        return ResponseEntity.ok(repairService.createOrder(userId, request));
    }

    /**
     * 管理员或调度人员将工单指派给维修人员。
     */
    @PostMapping("/{orderId}/assign")
    public ResponseEntity<RepairOrder> assignOrder(@PathVariable Long orderId,
                                                   @RequestParam Long technicianId) {
        return ResponseEntity.ok(repairService.assignOrder(orderId, technicianId));
    }

    /**
     * 维修人员提交维修结果并标记工单为完成。
     */
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<RepairOrder> completeOrder(@PathVariable Long orderId,
                                                     @RequestParam Long technicianId,
                                                     @RequestBody String resultDesc) {
        return ResponseEntity.ok(repairService.completeOrder(orderId, technicianId, resultDesc));
    }

    /**
     * 报修人对工单执行评价。
     */
    @PostMapping("/{orderId}/comment")
    public ResponseEntity<Comment> commentOrder(@PathVariable Long orderId,
                                                @RequestParam Long userId,
                                                @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(repairService.commentOrder(orderId, userId, request));
    }

    /**
     * 报修人查看自己的工单列表。
     */
    @GetMapping("/my")
    public ResponseEntity<List<RepairOrder>> getMyOrders(@RequestParam Long userId) {
        return ResponseEntity.ok(repairService.getOrdersByReporter(userId));
    }

    /**
     * 维修人员查看分配给自己的工单。
     */
    @GetMapping("/assigned")
    public ResponseEntity<List<RepairOrder>> getAssignedOrders(@RequestParam Long technicianId) {
        return ResponseEntity.ok(repairService.getOrdersByTechnician(technicianId));
    }

    /**
     * 管理员查询全部工单。
     */
    @GetMapping
    public ResponseEntity<List<RepairOrder>> getAllOrders() {
        return ResponseEntity.ok(repairService.getAllOrders());
    }
}
