package com.lochside.hotel.booking.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.lochside.hotel.booking.RoomDetails;
import com.lochside.hotel.booking.model.Booking;

public interface HotelBookingService {

	Booking save(Booking booking);

	Collection<Booking> findBookingByCustomerId(Long customerId);

	Collection<Booking> findBookingByRoomId(Long roomId);

	List<RoomDetails> findAvailableRooms(Date fromDate, Date toDate);

}
