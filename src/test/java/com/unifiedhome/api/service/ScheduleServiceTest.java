package com.unifiedhome.api.service;

import com.unifiedhome.api.dto.schedule.ScheduleCreateRequest;
import com.unifiedhome.api.dto.schedule.ScheduleResponse;
import com.unifiedhome.api.dto.schedule.ScheduleUpdateRequest;
import com.unifiedhome.api.exception.ResourceNotFoundException;
import com.unifiedhome.api.model.Device;
import com.unifiedhome.api.model.DeviceType;
import com.unifiedhome.api.model.Room;
import com.unifiedhome.api.model.Schedule;
import com.unifiedhome.api.model.ScheduleAction;
import com.unifiedhome.api.repository.DeviceRepository;
import com.unifiedhome.api.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private DeviceRepository deviceRepository;

    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleService = new ScheduleService(
                scheduleRepository,
                deviceRepository
        );
    }

    @Test
    void getAllSchedulesReturnsAllSchedules() {
        Room room = new Room("Living Room");

        Device device = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        Schedule schedule = new Schedule(
                device,
                ScheduleAction.TURN_ON,
                LocalTime.of(18, 0)
        );

        when(scheduleRepository.findAll())
                .thenReturn(List.of(schedule));

        List<ScheduleResponse> result =
                scheduleService.getAllSchedules();

        assertEquals(1, result.size());
        assertEquals(
                ScheduleAction.TURN_ON,
                result.get(0).getAction()
        );
        assertEquals(
                LocalTime.of(18, 0),
                result.get(0).getTime()
        );

        verify(scheduleRepository).findAll();
    }

    @Test
    void getScheduleByIdReturnsScheduleWhenItExists() {
        Room room = new Room("Living Room");

        Device device = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        Schedule schedule = new Schedule(
                device,
                ScheduleAction.TURN_ON,
                LocalTime.of(18, 0)
        );

        when(scheduleRepository.findById(1L))
                .thenReturn(Optional.of(schedule));

        ScheduleResponse result =
                scheduleService.getScheduleById(1L);

        assertEquals(
                ScheduleAction.TURN_ON,
                result.getAction()
        );
        assertEquals(
                LocalTime.of(18, 0),
                result.getTime()
        );

        verify(scheduleRepository).findById(1L);
    }

    @Test
    void getScheduleByIdThrowsExceptionWhenScheduleDoesNotExist() {
        when(scheduleRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> scheduleService.getScheduleById(999L)
        );

        verify(scheduleRepository).findById(999L);
    }

    @Test
    void createScheduleSavesScheduleWhenDeviceExists() {
        Room room = new Room("Living Room");

        Device device = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        ScheduleCreateRequest request =
                new ScheduleCreateRequest();

        request.setDeviceId(1L);
        request.setAction(ScheduleAction.TURN_ON);
        request.setTime(LocalTime.of(18, 0));

        Schedule savedSchedule = new Schedule(
                device,
                ScheduleAction.TURN_ON,
                LocalTime.of(18, 0)
        );

        when(deviceRepository.findById(1L))
                .thenReturn(Optional.of(device));

        when(scheduleRepository.save(any(Schedule.class)))
                .thenReturn(savedSchedule);

        ScheduleResponse result =
                scheduleService.createSchedule(request);

        assertEquals(
                ScheduleAction.TURN_ON,
                result.getAction()
        );
        assertEquals(
                LocalTime.of(18, 0),
                result.getTime()
        );

        verify(deviceRepository).findById(1L);
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void createScheduleThrowsExceptionWhenDeviceDoesNotExist() {
        ScheduleCreateRequest request =
                new ScheduleCreateRequest();

        request.setDeviceId(999L);
        request.setAction(ScheduleAction.TURN_ON);
        request.setTime(LocalTime.of(18, 0));

        when(deviceRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> scheduleService.createSchedule(request)
        );

        verify(deviceRepository).findById(999L);
        verify(scheduleRepository, never())
                .save(any(Schedule.class));
    }

    @Test
    void updateScheduleChangesScheduleDetails() {
        Room room = new Room("Living Room");

        Device originalDevice = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        Device newDevice = new Device(
                "Bedroom Light",
                DeviceType.LIGHT,
                room
        );

        Schedule schedule = new Schedule(
                originalDevice,
                ScheduleAction.TURN_ON,
                LocalTime.of(18, 0)
        );

        ScheduleUpdateRequest request =
                new ScheduleUpdateRequest();

        request.setDeviceId(2L);
        request.setAction(ScheduleAction.TURN_OFF);
        request.setTime(LocalTime.of(22, 0));

        when(scheduleRepository.findById(1L))
                .thenReturn(Optional.of(schedule));

        when(deviceRepository.findById(2L))
                .thenReturn(Optional.of(newDevice));

        when(scheduleRepository.save(schedule))
                .thenReturn(schedule);

        ScheduleResponse result =
                scheduleService.updateSchedule(1L, request);

        assertEquals(
                ScheduleAction.TURN_OFF,
                result.getAction()
        );
        assertEquals(
                LocalTime.of(22, 0),
                result.getTime()
        );

        verify(scheduleRepository).findById(1L);
        verify(deviceRepository).findById(2L);
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void updateScheduleThrowsExceptionWhenScheduleDoesNotExist() {
        ScheduleUpdateRequest request =
                new ScheduleUpdateRequest();

        request.setDeviceId(1L);
        request.setAction(ScheduleAction.TURN_ON);
        request.setTime(LocalTime.of(18, 0));

        when(scheduleRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> scheduleService.updateSchedule(999L, request)
        );

        verify(scheduleRepository).findById(999L);
        verify(deviceRepository, never()).findById(anyLong());
        verify(scheduleRepository, never())
                .save(any(Schedule.class));
    }

    @Test
    void updateScheduleThrowsExceptionWhenDeviceDoesNotExist() {
        Room room = new Room("Living Room");

        Device device = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        Schedule schedule = new Schedule(
                device,
                ScheduleAction.TURN_ON,
                LocalTime.of(18, 0)
        );

        ScheduleUpdateRequest request =
                new ScheduleUpdateRequest();

        request.setDeviceId(999L);
        request.setAction(ScheduleAction.TURN_OFF);
        request.setTime(LocalTime.of(22, 0));

        when(scheduleRepository.findById(1L))
                .thenReturn(Optional.of(schedule));

        when(deviceRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> scheduleService.updateSchedule(1L, request)
        );

        verify(scheduleRepository).findById(1L);
        verify(deviceRepository).findById(999L);
        verify(scheduleRepository, never())
                .save(any(Schedule.class));
    }

    @Test
    void deleteScheduleDeletesExistingSchedule() {
        when(scheduleRepository.existsById(1L))
                .thenReturn(true);

        scheduleService.deleteSchedule(1L);

        verify(scheduleRepository).existsById(1L);
        verify(scheduleRepository).deleteById(1L);
    }

    @Test
    void deleteScheduleThrowsExceptionWhenScheduleDoesNotExist() {
        when(scheduleRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> scheduleService.deleteSchedule(999L)
        );

        verify(scheduleRepository).existsById(999L);
        verify(scheduleRepository, never())
                .deleteById(999L);
    }
    
}