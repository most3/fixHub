package com.fixhub.repair.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建报修单的请求参数。
 */
public class CreateOrderRequest {

    private Long deviceId;

    private String deviceName;

    @NotBlank(message = "报修地点不能为空")
    private String location;

    @NotBlank(message = "故障描述不能为空")
    private String description;

    private String imageUrl;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
