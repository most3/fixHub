package com.fixhub.device.service;

import com.fixhub.device.mapper.DeviceMapper;
import com.fixhub.device.model.Device;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class DeviceService {

    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    public List<Device> getAllDevices() {
        return deviceMapper.selectAll();
    }

    @Transactional
    public Device createOrUpdateDevice(Device device) {
        Instant now = Instant.now();
        if (device.getId() == null) {
            device.setCreatedAt(now);
            device.setUpdatedAt(now);
            deviceMapper.insert(device);
        } else {
            device.setUpdatedAt(now);
            deviceMapper.update(device);
        }
        return deviceMapper.selectById(device.getId());
    }
}
