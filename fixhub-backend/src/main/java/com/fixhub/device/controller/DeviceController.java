package com.fixhub.device.controller;

import com.fixhub.device.model.Device;
import com.fixhub.device.repository.DeviceRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
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

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * 获取全部设备列表。
     */
    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    /**
     * 新增或保存一个设备。
     */
    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceRepository.save(device));
    }
}
