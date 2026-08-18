package com.unifiedhome.api.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Enumerated(EnumType.STRING)
    private ScheduleAction action;

    private LocalTime time;

    public Schedule() {
    }

    public Schedule(
            Device device,
            ScheduleAction action,
            LocalTime time) {

        this.device = device;
        this.action = action;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public Device getDevice() {
        return device;
    }

    public ScheduleAction getAction() {
        return action;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setAction(ScheduleAction action) {
        this.action = action;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
    
}