package com.fixhub.device.controller;

import com.fixhub.common.response.Result;
import com.fixhub.device.model.Device;
import com.fixhub.device.service.DeviceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备管理接口，提供设备查询与新增能力。
 */
@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * 获取全部设备列表。
     */
    @GetMapping
    public Result<List<Device>> getAllDevices() {
        return Result.success(deviceService.getAllDevices());
    }

    /**
     * 新增或保存一个设备。
     */
    @PostMapping
    public Result<Device> createDevice(@RequestBody Device device) {
        return Result.success(deviceService.createOrUpdateDevice(device));
    }
}
