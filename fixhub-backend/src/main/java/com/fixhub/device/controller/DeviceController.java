package com.fixhub.device.controller;

import com.fixhub.device.mapper.DeviceMapper;
import com.fixhub.device.model.Device;
import java.time.Instant;
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

    private final DeviceMapper deviceMapper;

    public DeviceController(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    /**
     * 获取全部设备列表。
     */
    @GetMapping
    public List<Device> getAllDevices() {
        return deviceMapper.selectAll();
    }

    /**
     * 新增或保存一个设备。
     */
    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        Instant now = Instant.now();
        if (device.getId() == null) {
            device.setCreatedAt(now);
            device.setUpdatedAt(now);
            deviceMapper.insert(device);
        } else {
            device.setUpdatedAt(now);
            deviceMapper.update(device);
        }
        Device saved = deviceMapper.selectById(device.getId());
        return ResponseEntity.ok(saved != null ? saved : device);
    }
}
