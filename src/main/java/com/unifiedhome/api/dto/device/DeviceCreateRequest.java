package com.unifiedhome.api.dto.device;

import com.unifiedhome.api.model.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DeviceCreateRequest {

    @NotBlank(message = "Device name cannot be blank")
    private String name;

    @NotNull(message = "Device type is required")
    private DeviceType type;

    @NotNull(message = "Room ID is required")
    private Long roomId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
}