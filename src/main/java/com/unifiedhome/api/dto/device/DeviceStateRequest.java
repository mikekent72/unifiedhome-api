package com.unifiedhome.api.dto.device;

import jakarta.validation.constraints.NotNull;

public class DeviceStateRequest {

    @NotNull(message = "Enabled state is required")
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
}
