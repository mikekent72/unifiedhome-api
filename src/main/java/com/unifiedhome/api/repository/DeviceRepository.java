package com.unifiedhome.api.repository;

import com.unifiedhome.api.model.Device;
import com.unifiedhome.api.model.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByType(DeviceType type);

    List<Device> findByRoomId(Long roomId);
    
}
