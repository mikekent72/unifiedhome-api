package com.unifiedhome.api.controller;

import com.unifiedhome.api.dto.schedule.ScheduleCreateRequest;
import com.unifiedhome.api.dto.schedule.ScheduleResponse;
import com.unifiedhome.api.dto.schedule.ScheduleUpdateRequest;
import com.unifiedhome.api.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<ScheduleResponse> getSchedules() {
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                scheduleService.getScheduleById(id)
        );
    }

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @Valid @RequestBody ScheduleCreateRequest request) {

        ScheduleResponse createdSchedule =
                scheduleService.createSchedule(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdSchedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdateRequest request) {

        return ResponseEntity.ok(
                scheduleService.updateSchedule(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id) {

        scheduleService.deleteSchedule(id);

        return ResponseEntity.noContent().build();
    }
    
}