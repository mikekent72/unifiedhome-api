package com.unifiedhome.api.dto.room;

import jakarta.validation.constraints.NotBlank;

public class RoomUpdateRequest {

    @NotBlank(message = "Room name cannot be blank")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}