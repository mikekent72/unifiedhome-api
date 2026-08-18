package com.unifiedhome.api.service;

import com.unifiedhome.api.dto.schedule.ScheduleCreateRequest;
import com.unifiedhome.api.dto.schedule.ScheduleResponse;
import com.unifiedhome.api.dto.schedule.ScheduleUpdateRequest;
import com.unifiedhome.api.exception.ResourceNotFoundException;
import com.unifiedhome.api.model.Device;
import com.unifiedhome.api.model.Schedule;
import com.unifiedhome.api.repository.DeviceRepository;
import com.unifiedhome.api.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DeviceRepository deviceRepository;

    public ScheduleService(
            ScheduleRepository scheduleRepository,
            DeviceRepository deviceRepository) {

        this.scheduleRepository = scheduleRepository;
        this.deviceRepository = deviceRepository;
    }

    public List<ScheduleResponse> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ScheduleResponse getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule with id " + id + " not found"));

        return toResponse(schedule);
    }

    public ScheduleResponse createSchedule(
            ScheduleCreateRequest request) {

        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Device with id " +
                                request.getDeviceId() +
                                " not found"));

        Schedule schedule = new Schedule(
                device,
                request.getAction(),
                request.getTime()
        );

        Schedule savedSchedule =
                scheduleRepository.save(schedule);

        return toResponse(savedSchedule);
    }

    public ScheduleResponse updateSchedule(
            Long id,
            ScheduleUpdateRequest request) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule with id " + id + " not found"));

        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Device with id " +
                                request.getDeviceId() +
                                " not found"));

        schedule.setDevice(device);
        schedule.setAction(request.getAction());
        schedule.setTime(request.getTime());

        Schedule updatedSchedule =
                scheduleRepository.save(schedule);

        return toResponse(updatedSchedule);
    }

    public void deleteSchedule(Long id) {

        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Schedule with id " + id + " not found");
        }

        scheduleRepository.deleteById(id);
    }

    private ScheduleResponse toResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getDevice().getId(),
                schedule.getAction(),
                schedule.getTime()
        );
    }
    
}
