package com.fixhub.repair.service;

import cn.hutool.core.util.RandomUtil;
import com.fixhub.common.exception.UnauthorizedException;
import com.fixhub.device.mapper.DeviceMapper;
import com.fixhub.device.model.Device;
import com.fixhub.repair.dto.CreateCommentRequest;
import com.fixhub.repair.dto.CreateOrderRequest;
import com.fixhub.repair.model.Comment;
import com.fixhub.repair.model.RepairOrder;
import com.fixhub.repair.model.RepairOrder.OrderStatus;
import com.fixhub.repair.mapper.CommentMapper;
import com.fixhub.repair.mapper.RepairOrderMapper;
import com.fixhub.user.model.User;
import com.fixhub.user.model.enums.UserRole;
import com.fixhub.user.mapper.UserMapper;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 维修工单服务，负责报修单从创建到评价的业务流转。
 * 包含创建、指派、完工、评价等关键节点的校验逻辑。
 */
@Service
public class RepairService {

    private final RepairOrderMapper repairOrderMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final DeviceMapper deviceMapper;

    /**
     * 通过构造器注入依赖，便于测试和维护。
     */
    public RepairService(RepairOrderMapper repairOrderMapper,
                         CommentMapper commentMapper,
                         UserMapper userMapper,
                         DeviceMapper deviceMapper) {
        this.repairOrderMapper = repairOrderMapper;
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
        this.deviceMapper = deviceMapper;
    }

    /**
     * 创建报修单，允许选择设备或手工填写设备名称。
     * @param reporterId 报修人编号
     * @param request    报修单请求参数
     * @return 新建的报修单实体
     */
    @Transactional
    public RepairOrder createOrder(Long reporterId, CreateOrderRequest request) {
        User reporter = userMapper.selectById(reporterId);
        if (reporter == null) {
            throw new IllegalArgumentException("未找到报修人");
        }

        RepairOrder order = new RepairOrder();
        // 生成 12 位大写流水号，方便人工识别
        order.setOrderNo(RandomUtil.randomStringUpper(12));
        order.setReporter(reporter);
        order.setLocation(request.getLocation());
        order.setDescription(request.getDescription());
        order.setImageUrl(request.getImageUrl());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(Instant.now());

        if (request.getDeviceId() != null) {
            Device device = deviceMapper.selectById(request.getDeviceId());
            if (device == null) {
                throw new IllegalArgumentException("未找到设备");
            }
            order.setDevice(device);
            order.setDeviceName(device.getName());
        } else {
            // 未选择设备时保留用户手动录入的名称
            order.setDeviceName(request.getDeviceName());
        }

        repairOrderMapper.insert(order);
        return repairOrderMapper.selectById(order.getId());
    }

    /**
     * 将报修单指派给指定维修工。
     * @param orderId       报修单编号
     * @param technicianId  维修工编号
     * @return 更新后的报修单
     */
    @Transactional
    public RepairOrder assignOrder(Long orderId, Long technicianId) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("未找到报修单");
        }
        User technician = userMapper.selectById(technicianId);
        if (technician == null) {
            throw new IllegalArgumentException("未找到维修工");
        }

        if (technician.getRole() != UserRole.TECHNICIAN) {
            throw new IllegalArgumentException("当前用户不是维修工角色");
        }

        Instant now = Instant.now();
        repairOrderMapper.updateAssignment(orderId, technicianId, now, OrderStatus.ASSIGNED.name());
        return repairOrderMapper.selectById(orderId);
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
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("未找到报修单");
        }

        if (order.getTechnician() == null || !order.getTechnician().getId().equals(technicianId)) {
            throw new UnauthorizedException("当前维修单未指派给该维修工");
        }

        Instant now = Instant.now();
        repairOrderMapper.updateCompletion(orderId, resultDesc, now, OrderStatus.REPAIRED.name());
        return repairOrderMapper.selectById(orderId);
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
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("未找到报修单");
        }

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
        comment.setCreatedAt(Instant.now());

        // 评价完成后将工单状态置为已关闭
        Instant now = Instant.now();
        repairOrderMapper.updateClosure(orderId, now, OrderStatus.CLOSED.name());
        commentMapper.insert(comment);
        return commentMapper.selectById(comment.getId());
    }

    /**
     * 查询报修人提交的所有工单。
     */
    public List<RepairOrder> getOrdersByReporter(Long reporterId) {
        return repairOrderMapper.selectByReporterId(reporterId);
    }

    /**
     * 查询指派给指定维修工的工单列表。
     */
    public List<RepairOrder> getOrdersByTechnician(Long technicianId) {
        return repairOrderMapper.selectByTechnicianId(technicianId);
    }

    /**
     * 查询系统中的全部工单，通常用于管理员查看。
     */
    public List<RepairOrder> getAllOrders() {
        return repairOrderMapper.selectAll();
    }
}
