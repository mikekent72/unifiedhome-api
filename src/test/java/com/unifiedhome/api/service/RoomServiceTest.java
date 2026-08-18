package com.unifiedhome.api.service;

import com.unifiedhome.api.dto.room.RoomCreateRequest;
import com.unifiedhome.api.dto.room.RoomResponse;
import com.unifiedhome.api.dto.room.RoomUpdateRequest;
import com.unifiedhome.api.exception.ResourceNotFoundException;
import com.unifiedhome.api.model.Room;
import com.unifiedhome.api.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomService(roomRepository);
    }

    @Test
    void getAllRoomsReturnsAllRooms() {
        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");

        when(roomRepository.findAll())
                .thenReturn(List.of(livingRoom, bedroom));

        List<RoomResponse> result = roomService.getAllRooms();

        assertEquals(2, result.size());
        assertEquals("Living Room", result.get(0).getName());
        assertEquals("Bedroom", result.get(1).getName());

        verify(roomRepository).findAll();
    }

    @Test
    void getRoomByIdReturnsRoomWhenRoomExists() {
        Room room = new Room("Living Room");

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        RoomResponse result = roomService.getRoomById(1L);

        assertEquals("Living Room", result.getName());

        verify(roomRepository).findById(1L);
    }

    @Test
    void getRoomByIdThrowsExceptionWhenRoomDoesNotExist() {
        when(roomRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> roomService.getRoomById(999L)
        );

        verify(roomRepository).findById(999L);
    }

    @Test
    void createRoomSavesRoom() {
        RoomCreateRequest request = new RoomCreateRequest();
        request.setName("Kitchen");

        Room savedRoom = new Room("Kitchen");

        when(roomRepository.save(any(Room.class)))
                .thenReturn(savedRoom);

        RoomResponse result = roomService.createRoom(request);

        assertEquals("Kitchen", result.getName());

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void updateRoomChangesRoomName() {
        Room existingRoom = new Room("Bedroom");

        RoomUpdateRequest request = new RoomUpdateRequest();
        request.setName("Main Bedroom");

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(existingRoom));

        when(roomRepository.save(existingRoom))
                .thenReturn(existingRoom);

        RoomResponse result = roomService.updateRoom(1L, request);

        assertEquals("Main Bedroom", result.getName());

        verify(roomRepository).findById(1L);
        verify(roomRepository).save(existingRoom);
    }

    @Test
    void updateRoomThrowsExceptionWhenRoomDoesNotExist() {
        RoomUpdateRequest request = new RoomUpdateRequest();
        request.setName("Main Bedroom");

        when(roomRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> roomService.updateRoom(999L, request)
        );

        verify(roomRepository).findById(999L);
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void deleteRoomDeletesExistingRoom() {
        when(roomRepository.existsById(1L))
                .thenReturn(true);

        roomService.deleteRoom(1L);

        verify(roomRepository).existsById(1L);
        verify(roomRepository).deleteById(1L);
    }

    @Test
    void deleteRoomThrowsExceptionWhenRoomDoesNotExist() {
        when(roomRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> roomService.deleteRoom(999L)
        );

        verify(roomRepository).existsById(999L);
        verify(roomRepository, never()).deleteById(999L);
    }

}
