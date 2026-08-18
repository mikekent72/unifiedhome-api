package com.unifiedhome.api.service;

import com.unifiedhome.api.dto.device.DeviceCreateRequest;
import com.unifiedhome.api.dto.device.DeviceResponse;
import com.unifiedhome.api.dto.device.DeviceUpdateRequest;
import com.unifiedhome.api.exception.ResourceNotFoundException;
import com.unifiedhome.api.model.Device;
import com.unifiedhome.api.model.DeviceType;
import com.unifiedhome.api.model.Room;
import com.unifiedhome.api.repository.DeviceRepository;
import com.unifiedhome.api.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;

    public DeviceService(
            DeviceRepository deviceRepository,
            RoomRepository roomRepository) {

        this.deviceRepository = deviceRepository;
        this.roomRepository = roomRepository;
    }

    public List<DeviceResponse> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DeviceResponse getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Device with id " + id + " not found"));

        return toResponse(device);
    }

    public DeviceResponse createDevice(DeviceCreateRequest request) {

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room with id " +
                                request.getRoomId() +
                                " not found"));

        Device device = new Device(
                request.getName(),
                request.getType(),
                room
        );

        Device savedDevice = deviceRepository.save(device);

        return toResponse(savedDevice);
    }

    public DeviceResponse updateDevice(
            Long id,
            DeviceUpdateRequest request) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Device with id " + id + " not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room with id " +
                                request.getRoomId() +
                                " not found"));

        device.setName(request.getName());
        device.setType(request.getType());
        device.setRoom(room);

        Device updatedDevice = deviceRepository.save(device);

        return toResponse(updatedDevice);
    }

    public void deleteDevice(Long id) {

        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Device with id " + id + " not found");
        }

        deviceRepository.deleteById(id);
    }

    public List<DeviceResponse> getDevicesByType(DeviceType type) {
        return deviceRepository.findByType(type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DeviceResponse> getDevicesByRoom(Long roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException(
                    "Room with id " + roomId + " not found");
        }

        return deviceRepository.findByRoomId(roomId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private DeviceResponse toResponse(Device device) {
        return new DeviceResponse(
                device.getId(),
                device.getName(),
                device.getType(),
                device.getRoom().getId(),
                device.isEnabled()
        );
    }
    
}