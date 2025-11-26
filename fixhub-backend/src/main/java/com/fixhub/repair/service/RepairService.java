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

/**
 * 维修工单服务，负责报修单从创建到评价的业务流转。
 * 包含创建、指派、完工、评价等关键节点的校验逻辑。
 */
@Service
public class RepairService {

    private final RepairOrderRepository orderRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    /**
     * 通过构造器注入依赖，便于测试和维护。
     */
    public RepairService(RepairOrderRepository orderRepository,
                         CommentRepository commentRepository,
                         UserRepository userRepository,
                         DeviceRepository deviceRepository) {
        this.orderRepository = orderRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * 创建报修单，允许选择设备或手工填写设备名称。
     * @param reporterId 报修人编号
     * @param request    报修单请求参数
     * @return 新建的报修单实体
     */
    @Transactional
    public RepairOrder createOrder(Long reporterId, CreateOrderRequest request) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("未找到报修人"));

        RepairOrder order = new RepairOrder();
        // 生成 12 位大写流水号，方便人工识别
        order.setOrderNo(UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        order.setReporter(reporter);
        order.setLocation(request.getLocation());
        order.setDescription(request.getDescription());
        order.setImageUrl(request.getImageUrl());
        order.setStatus(OrderStatus.PENDING);

        if (request.getDeviceId() != null) {
            Device device = deviceRepository.findById(request.getDeviceId())
                    .orElseThrow(() -> new IllegalArgumentException("未找到设备"));
            order.setDevice(device);
            order.setDeviceName(device.getName());
        } else {
            // 未选择设备时保留用户手动录入的名称
            order.setDeviceName(request.getDeviceName());
        }

        return orderRepository.save(order);
    }

    /**
     * 将报修单指派给指定维修工。
     * @param orderId       报修单编号
     * @param technicianId  维修工编号
     * @return 更新后的报修单
     */
    @Transactional
    public RepairOrder assignOrder(Long orderId, Long technicianId) {
        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("未找到报修单"));
        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new IllegalArgumentException("未找到维修工"));

        if (technician.getRole() != UserRole.TECHNICIAN) {
            throw new IllegalArgumentException("当前用户不是维修工角色");
        }

        order.setTechnician(technician);
        order.setStatus(OrderStatus.ASSIGNED);
        order.setAssignedAt(Instant.now());
        return orderRepository.save(order);
    }

    /**
     * 维修工填写处理结果并标记工单为已维修。
     * @param orderId      报修单编号
     * @param technicianId 当前操作的维修工编号
     * @param resultDesc   处理结果描述
     * @return 更新后的报修单
     */
    @Transactional
    public RepairOrder completeOrder(Long orderId, Long technicianId, String resultDesc) {
        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("未找到报修单"));

        if (!order.getTechnician().getId().equals(technicianId)) {
            throw new UnauthorizedException("当前维修单未指派给该维修工");
        }

        order.setResultDesc(resultDesc);
        order.setStatus(OrderStatus.REPAIRED);
        order.setRepairedAt(Instant.now());
        return orderRepository.save(order);
    }

    /**
     * 报修人对工单进行评价，并同步关闭工单。
     * @param orderId 工单编号
     * @param userId  当前登录用户编号
     * @param request 评价内容
     * @return 新增的评价记录
     */
    @Transactional
    public Comment commentOrder(Long orderId, Long userId, CreateCommentRequest request) {
        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("未找到报修单"));

        if (!order.getReporter().getId().equals(userId)) {
            throw new UnauthorizedException("仅报修人可以评价该工单");
        }
        if (order.getStatus() != OrderStatus.REPAIRED) {
            throw new IllegalStateException("报修单尚未完成维修，无法评价");
        }

        Comment comment = new Comment();
        comment.setOrder(order);
        comment.setUser(order.getReporter());
        comment.setRating(request.getRating());
        comment.setContent(request.getContent());

        // 评价完成后将工单状态置为已关闭
        order.setStatus(OrderStatus.CLOSED);
        order.setClosedAt(Instant.now());
        orderRepository.save(order);

        return commentRepository.save(comment);
    }

    /**
     * 查询报修人提交的所有工单。
     */
    public List<RepairOrder> getOrdersByReporter(Long reporterId) {
        return orderRepository.findByReporterId(reporterId);
    }

    /**
     * 查询指派给指定维修工的工单列表。
     */
    public List<RepairOrder> getOrdersByTechnician(Long technicianId) {
        return orderRepository.findByTechnicianId(technicianId);
    }

    /**
     * 查询系统中的全部工单，通常用于管理员查看。
     */
    public List<RepairOrder> getAllOrders() {
        return orderRepository.findAll();
    }
}
