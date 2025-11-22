package com.fixhub.repair.service;

import com.fixhub.common.exception.UnauthorizedException;
import com.fixhub.device.model.Device;
import com.fixhub.device.repository.DeviceRepository;
import com.fixhub.repair.dto.CreateCommentRequest;
import com.fixhub.repair.dto.CreateOrderRequest;
import com.fixhub.repair.model.Comment;
import com.fixhub.repair.model.RepairOrder;
import com.fixhub.repair.model.RepairOrder.OrderStatus;
import com.fixhub.repair.repository.CommentRepository;
import com.fixhub.repair.repository.RepairOrderRepository;
import com.fixhub.user.model.User;
import com.fixhub.user.model.enums.UserRole;
import com.fixhub.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RepairService {

    private final RepairOrderRepository orderRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    public RepairService(RepairOrderRepository orderRepository,
                         CommentRepository commentRepository,
                         UserRepository userRepository,
                         DeviceRepository deviceRepository) {
        this.orderRepository = orderRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public RepairOrder createOrder(Long reporterId, CreateOrderRequest request) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        RepairOrder order = new RepairOrder();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        order.setReporter(reporter);
        order.setLocation(request.getLocation());
        order.setDescription(request.getDescription());
        order.setImageUrl(request.getImageUrl());
        order.setStatus(OrderStatus.PENDING);

        if (request.getDeviceId() != null) {
            Device device = deviceRepository.findById(request.getDeviceId())
                    .orElseThrow(() -> new IllegalArgumentException("Device not found"));
            order.setDevice(device);
            order.setDeviceName(device.getName());
        } else {
            order.setDeviceName(request.getDeviceName());
        }

        return orderRepository.save(order);
    }

    @Transactional
    public RepairOrder assignOrder(Long orderId, Long technicianId) {
        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new IllegalArgumentException("Technician not found"));

        if (technician.getRole() != UserRole.TECHNICIAN) {
            throw new IllegalArgumentException("User is not a technician");
        }

        order.setTechnician(technician);
        order.setStatus(OrderStatus.ASSIGNED);
        order.setAssignedAt(Instant.now());
        return orderRepository.save(order);
    }

    @Transactional
    public RepairOrder completeOrder(Long orderId, Long technicianId, String resultDesc) {
        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getTechnician().getId().equals(technicianId)) {
            throw new UnauthorizedException("Not assigned to this technician");
        }

        order.setResultDesc(resultDesc);
        order.setStatus(OrderStatus.REPAIRED);
        order.setRepairedAt(Instant.now());
        return orderRepository.save(order);
    }

    @Transactional
    public Comment commentOrder(Long orderId, Long userId, CreateCommentRequest request) {
        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getReporter().getId().equals(userId)) {
            throw new UnauthorizedException("Only reporter can comment");
        }
        if (order.getStatus() != OrderStatus.REPAIRED) {
            throw new IllegalStateException("Order is not repaired yet");
        }

        Comment comment = new Comment();
        comment.setOrder(order);
        comment.setUser(order.getReporter());
        comment.setRating(request.getRating());
        comment.setContent(request.getContent());
        
        order.setStatus(OrderStatus.CLOSED);
        order.setClosedAt(Instant.now());
        orderRepository.save(order);

        return commentRepository.save(comment);
    }

    public List<RepairOrder> getOrdersByReporter(Long reporterId) {
        return orderRepository.findByReporterId(reporterId);
    }

    public List<RepairOrder> getOrdersByTechnician(Long technicianId) {
        return orderRepository.findByTechnicianId(technicianId);
    }

    public List<RepairOrder> getAllOrders() {
        return orderRepository.findAll();
    }
}
