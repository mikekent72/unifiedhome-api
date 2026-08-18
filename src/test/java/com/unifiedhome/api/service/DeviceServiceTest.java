package com.unifiedhome.api.service;

import com.unifiedhome.api.dto.device.DeviceCreateRequest;
import com.unifiedhome.api.dto.device.DeviceResponse;
import com.unifiedhome.api.dto.device.DeviceStateRequest;
import com.unifiedhome.api.dto.device.DeviceUpdateRequest;
import com.unifiedhome.api.exception.ResourceNotFoundException;
import com.unifiedhome.api.model.Device;
import com.unifiedhome.api.model.DeviceType;
import com.unifiedhome.api.model.Room;
import com.unifiedhome.api.repository.DeviceRepository;
import com.unifiedhome.api.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private RoomRepository roomRepository;

    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceService = new DeviceService(
                deviceRepository,
                roomRepository
        );
    }

    @Test
    void getAllDevicesReturnsAllDevices() {
        Room room = new Room("Living Room");

        Device light = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        Device fan = new Device(
                "Living Room Fan",
                DeviceType.FAN,
                room
        );

        when(deviceRepository.findAll())
                .thenReturn(List.of(light, fan));

        List<DeviceResponse> result =
                deviceService.getAllDevices();

        assertEquals(2, result.size());
        assertEquals(
                "Living Room Light",
                result.get(0).getName()
        );
        assertEquals(
                "Living Room Fan",
                result.get(1).getName()
        );

        verify(deviceRepository).findAll();
    }

    @Test
    void getDeviceByIdReturnsDeviceWhenDeviceExists() {
        Room room = new Room("Living Room");

        Device device = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        when(deviceRepository.findById(1L))
                .thenReturn(Optional.of(device));

        DeviceResponse result =
                deviceService.getDeviceById(1L);

        assertEquals(
                "Living Room Light",
                result.getName()
        );
        assertEquals(
                DeviceType.LIGHT,
                result.getType()
        );
        assertFalse(result.isEnabled());

        verify(deviceRepository).findById(1L);
    }

    @Test
    void getDeviceByIdThrowsExceptionWhenDeviceDoesNotExist() {
        when(deviceRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.getDeviceById(999L)
        );

        verify(deviceRepository).findById(999L);
    }

    @Test
    void createDeviceSavesDeviceWhenRoomExists() {
        Room room = new Room("Living Room");

        DeviceCreateRequest request =
                new DeviceCreateRequest();

        request.setName("Living Room Light");
        request.setType(DeviceType.LIGHT);
        request.setRoomId(1L);

        Device savedDevice = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(deviceRepository.save(any(Device.class)))
                .thenReturn(savedDevice);

        DeviceResponse result =
                deviceService.createDevice(request);

        assertEquals(
                "Living Room Light",
                result.getName()
        );
        assertEquals(
                DeviceType.LIGHT,
                result.getType()
        );

        verify(roomRepository).findById(1L);
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void createDeviceThrowsExceptionWhenRoomDoesNotExist() {
        DeviceCreateRequest request =
                new DeviceCreateRequest();

        request.setName("Living Room Light");
        request.setType(DeviceType.LIGHT);
        request.setRoomId(999L);

        when(roomRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.createDevice(request)
        );

        verify(roomRepository).findById(999L);
        verify(deviceRepository, never())
                .save(any(Device.class));
    }

    @Test
    void updateDeviceChangesDeviceDetails() {
        Room originalRoom = new Room("Living Room");
        Room newRoom = new Room("Bedroom");
        ReflectionTestUtils.setField(newRoom, "id", 2L);

        Device device = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                originalRoom
        );

        DeviceUpdateRequest request =
                new DeviceUpdateRequest();

        request.setName("Bedroom Light");
        request.setType(DeviceType.LIGHT);
        request.setRoomId(2L);

        when(deviceRepository.findById(1L))
                .thenReturn(Optional.of(device));

        when(roomRepository.findById(2L))
                .thenReturn(Optional.of(newRoom));

        when(deviceRepository.save(device))
                .thenReturn(device);

        DeviceResponse result =
                deviceService.updateDevice(1L, request);

        assertEquals("Bedroom Light", result.getName());
        assertEquals(2L, result.getRoomId());

        verify(deviceRepository).findById(1L);
        verify(roomRepository).findById(2L);
        verify(deviceRepository).save(device);
    }

    @Test
    void updateDeviceThrowsExceptionWhenDeviceDoesNotExist() {
        DeviceUpdateRequest request =
                new DeviceUpdateRequest();

        request.setName("Bedroom Light");
        request.setType(DeviceType.LIGHT);
        request.setRoomId(2L);

        when(deviceRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.updateDevice(999L, request)
        );

        verify(deviceRepository).findById(999L);
        verify(roomRepository, never()).findById(anyLong());
        verify(deviceRepository, never()).save(any(Device.class));
    }

    @Test
    void updateDeviceThrowsExceptionWhenNewRoomDoesNotExist() {
        Room room = new Room("Living Room");

        Device device = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        DeviceUpdateRequest request =
                new DeviceUpdateRequest();

        request.setName("Bedroom Light");
        request.setType(DeviceType.LIGHT);
        request.setRoomId(999L);

        when(deviceRepository.findById(1L))
                .thenReturn(Optional.of(device));

        when(roomRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.updateDevice(1L, request)
        );

        verify(deviceRepository).findById(1L);
        verify(roomRepository).findById(999L);
        verify(deviceRepository, never()).save(any(Device.class));
    }

    @Test
    void deleteDeviceDeletesExistingDevice() {
        when(deviceRepository.existsById(1L))
                .thenReturn(true);

        deviceService.deleteDevice(1L);

        verify(deviceRepository).existsById(1L);
        verify(deviceRepository).deleteById(1L);
    }

    @Test
    void deleteDeviceThrowsExceptionWhenDeviceDoesNotExist() {
        when(deviceRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.deleteDevice(999L)
        );

        verify(deviceRepository).existsById(999L);
        verify(deviceRepository, never()).deleteById(999L);
    }

    @Test
    void updateDeviceStateChangesEnabledState() {
        Room room = new Room("Living Room");

        Device device = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        DeviceStateRequest request =
                new DeviceStateRequest();

        request.setEnabled(true);

        when(deviceRepository.findById(1L))
                .thenReturn(Optional.of(device));

        when(deviceRepository.save(device))
                .thenReturn(device);

        DeviceResponse result =
                deviceService.updateDeviceState(1L, request);

        assertTrue(result.isEnabled());

        verify(deviceRepository).findById(1L);
        verify(deviceRepository).save(device);
    }

    @Test
    void updateDeviceStateThrowsExceptionWhenDeviceDoesNotExist() {
        DeviceStateRequest request =
                new DeviceStateRequest();

        request.setEnabled(true);

        when(deviceRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.updateDeviceState(999L, request)
        );

        verify(deviceRepository).findById(999L);
        verify(deviceRepository, never())
                .save(any(Device.class));
    }

    @Test
    void getDevicesByTypeReturnsMatchingDevices() {
        Room room = new Room("Living Room");

        Device light = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        when(deviceRepository.findByType(DeviceType.LIGHT))
                .thenReturn(List.of(light));

        List<DeviceResponse> result =
                deviceService.getDevicesByType(DeviceType.LIGHT);

        assertEquals(1, result.size());
        assertEquals(
                DeviceType.LIGHT,
                result.get(0).getType()
        );

        verify(deviceRepository)
                .findByType(DeviceType.LIGHT);
    }

    @Test
    void getDevicesByRoomReturnsDevicesWhenRoomExists() {
        Room room = new Room("Living Room");

        Device light = new Device(
                "Living Room Light",
                DeviceType.LIGHT,
                room
        );

        when(roomRepository.existsById(1L))
                .thenReturn(true);

        when(deviceRepository.findByRoomId(1L))
                .thenReturn(List.of(light));

        List<DeviceResponse> result =
                deviceService.getDevicesByRoom(1L);

        assertEquals(1, result.size());
        assertEquals(
                "Living Room Light",
                result.get(0).getName()
        );

        verify(roomRepository).existsById(1L);
        verify(deviceRepository).findByRoomId(1L);
    }

    @Test
    void getDevicesByRoomThrowsExceptionWhenRoomDoesNotExist() {
        when(roomRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.getDevicesByRoom(999L)
        );

        verify(roomRepository).existsById(999L);
        verify(deviceRepository, never())
                .findByRoomId(anyLong());
    }

}