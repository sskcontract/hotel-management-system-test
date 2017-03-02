package com.lochside.hotel.booking.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lochside.hotel.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, String> {
	Collection<Booking> findByCustomerId(Long customerId);

	Collection<Booking> findByRoomId(Long roomId);

}
