package com.unifiedhome.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public Device() {
    }

    public Device(String name, DeviceType type, Room room) {
        this.name = name;
        this.type = type;
        this.room = room;
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

    public boolean isEnabled() {
        return enabled;
    }

    public Room getRoom() {
        return room;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}