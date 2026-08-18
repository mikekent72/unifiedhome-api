package com.unifiedhome.api.controller;

import com.unifiedhome.api.dto.device.DeviceCreateRequest;
import com.unifiedhome.api.dto.device.DeviceResponse;
import com.unifiedhome.api.dto.device.DeviceUpdateRequest;
import com.unifiedhome.api.model.DeviceType;
import com.unifiedhome.api.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public List<DeviceResponse> getDevices(
            @RequestParam(required = false) DeviceType type,
            @RequestParam(required = false) Long room) {

        if (type != null) {
            return deviceService.getDevicesByType(type);
        }

        if (room != null) {
            return deviceService.getDevicesByRoom(room);
        }

        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDeviceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                deviceService.getDeviceById(id)
        );
    }

    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(
            @Valid @RequestBody DeviceCreateRequest request) {

        DeviceResponse createdDevice =
                deviceService.createDevice(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdDevice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceUpdateRequest request) {

        return ResponseEntity.ok(
                deviceService.updateDevice(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(
            @PathVariable Long id) {

        deviceService.deleteDevice(id);

        return ResponseEntity.noContent().build();
    }
    
}