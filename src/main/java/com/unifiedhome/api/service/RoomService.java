package com.unifiedhome.api.service;

import com.unifiedhome.api.dto.room.RoomCreateRequest;
import com.unifiedhome.api.dto.room.RoomResponse;
import com.unifiedhome.api.dto.room.RoomUpdateRequest;
import com.unifiedhome.api.exception.ResourceNotFoundException;
import com.unifiedhome.api.model.Room;
import com.unifiedhome.api.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room with id " + id + " not found"));

        return toResponse(room);
    }

    public RoomResponse createRoom(RoomCreateRequest request) {
        Room room = new Room(request.getName());

        Room savedRoom = roomRepository.save(room);

        return toResponse(savedRoom);
    }

    public RoomResponse updateRoom(Long id, RoomUpdateRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room with id " + id + " not found"));

        room.setName(request.getName());

        Room updatedRoom = roomRepository.save(room);

        return toResponse(updatedRoom);
    }

    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Room with id " + id + " not found");
        }

        roomRepository.deleteById(id);
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName()
        );
    }
    
}