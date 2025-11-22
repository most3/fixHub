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

@RestController
@RequestMapping("/api/repairs")
public class RepairController {

    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @PostMapping
    public ResponseEntity<RepairOrder> createOrder(@RequestParam Long userId,
                                                   @Valid @RequestBody CreateOrderRequest request) {
        // TODO: userId should be extracted from SecurityContext in real app
        return ResponseEntity.ok(repairService.createOrder(userId, request));
    }

    @PostMapping("/{orderId}/assign")
    public ResponseEntity<RepairOrder> assignOrder(@PathVariable Long orderId,
                                                   @RequestParam Long technicianId) {
        return ResponseEntity.ok(repairService.assignOrder(orderId, technicianId));
    }

    @PostMapping("/{orderId}/complete")
    public ResponseEntity<RepairOrder> completeOrder(@PathVariable Long orderId,
                                                     @RequestParam Long technicianId,
                                                     @RequestBody String resultDesc) {
        return ResponseEntity.ok(repairService.completeOrder(orderId, technicianId, resultDesc));
    }

    @PostMapping("/{orderId}/comment")
    public ResponseEntity<Comment> commentOrder(@PathVariable Long orderId,
                                                @RequestParam Long userId,
                                                @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(repairService.commentOrder(orderId, userId, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RepairOrder>> getMyOrders(@RequestParam Long userId) {
        return ResponseEntity.ok(repairService.getOrdersByReporter(userId));
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<RepairOrder>> getAssignedOrders(@RequestParam Long technicianId) {
        return ResponseEntity.ok(repairService.getOrdersByTechnician(technicianId));
    }

    @GetMapping
    public ResponseEntity<List<RepairOrder>> getAllOrders() {
        return ResponseEntity.ok(repairService.getAllOrders());
    }
}
