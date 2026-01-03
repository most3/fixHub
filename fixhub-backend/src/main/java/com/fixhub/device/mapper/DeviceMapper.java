package com.fixhub.device.mapper;

import com.fixhub.device.model.Device;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备持久化 Mapper。
 */
@Mapper
public interface DeviceMapper {

    Device selectById(@Param("id") Long id);

    List<Device> selectAll();

    int insert(Device device);

    int update(Device device);
}
