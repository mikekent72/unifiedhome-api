package com.unifiedhome.api.dto.schedule;

import com.unifiedhome.api.model.ScheduleAction;

import java.time.LocalTime;

public class ScheduleResponse {

    private Long id;
    private Long deviceId;
    private ScheduleAction action;
    private LocalTime time;

    public ScheduleResponse(
            Long id,
            Long deviceId,
            ScheduleAction action,
            LocalTime time) {

        this.id = id;
        this.deviceId = deviceId;
        this.action = action;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public ScheduleAction getAction() {
        return action;
    }

    public LocalTime getTime() {
        return time;
    }
    
}