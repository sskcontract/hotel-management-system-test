package com.lochside.hotel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lochside.hotel.booking.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}