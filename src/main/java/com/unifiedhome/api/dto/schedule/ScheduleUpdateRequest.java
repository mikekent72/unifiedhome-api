package com.unifiedhome.api.dto.schedule;

import com.unifiedhome.api.model.ScheduleAction;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class ScheduleUpdateRequest {

    @NotNull(message = "Device ID is required")
    private Long deviceId;

    @NotNull(message = "Schedule action is required")
    private ScheduleAction action;

    @NotNull(message = "Schedule time is required")
    private LocalTime time;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public ScheduleAction getAction() {
        return action;
    }

    public void setAction(ScheduleAction action) {
        this.action = action;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
    
}