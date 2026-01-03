package com.fixhub.repair.model;

import com.fixhub.device.model.Device;
import com.fixhub.user.model.User;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepairOrder {

    private Long id;

    private String orderNo;

    private User reporter;

    private Device device;

    private String deviceName;

    private String location;

    private String description;

    private String imageUrl;

    private OrderStatus status = OrderStatus.PENDING;

    private User technician;

    private String resultDesc;

    private Instant createdAt = Instant.now();

    private Instant assignedAt;

    private Instant repairedAt;

    private Instant closedAt;

    public enum OrderStatus {
        PENDING, // 待受理
        ASSIGNED, // 已派单/处理中
        REPAIRED, // 维修完成/待确认
        CLOSED // 已关闭/已评价
    }

}
