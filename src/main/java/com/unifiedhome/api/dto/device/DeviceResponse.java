package com.unifiedhome.api.dto.device;

import com.unifiedhome.api.model.DeviceType;

public class DeviceResponse {

    private Long id;
    private String name;
    private DeviceType type;
    private Long roomId;
    private boolean enabled;

    public DeviceResponse(
            Long id,
            String name,
            DeviceType type,
            Long roomId,
            boolean enabled) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.roomId = roomId;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DeviceType getType() {
        return type;
    }

    public Long getRoomId() {
        return roomId;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
}