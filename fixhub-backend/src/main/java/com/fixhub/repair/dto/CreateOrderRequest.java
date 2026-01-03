package com.fixhub.repair.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建报修单的请求参数。
 */
@Data
@NoArgsConstructor
public class CreateOrderRequest {

    private Long deviceId;

    private String deviceName;

    @NotBlank(message = "报修地点不能为空")
    private String location;

    @NotBlank(message = "故障描述不能为空")
    private String description;

    private String imageUrl;

}
