package com.lochside.hotel.booking.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lochside.hotel.booking.RoomDetails;
import com.lochside.hotel.booking.model.Booking;
import com.lochside.hotel.booking.service.HotelBookingService;

@RestController
@RequestMapping("/bookingDetails")
public class HotelBookingController {

	@Autowired
	private HotelBookingService bookingService;

	@RequestMapping( value = "/customer/{customerId}")
	public Collection<Booking> getBookingsByCustomer(@PathVariable Long customerId) {
		return bookingService.findBookingByCustomerId(customerId);
	}

	@RequestMapping(value = "/room/{roomId}")
	public Collection<Booking> getBookingsByRoom(@PathVariable Long roomId) {
		return bookingService.findBookingByRoomId(roomId);
	}

	@RequestMapping(value = "/availableRooms/{from_date}/{to_date}")
	public List<RoomDetails> getAvailableRooms(@PathVariable String from_date, @PathVariable String to_date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		try {
			return bookingService.findAvailableRooms(formatter.parse(from_date), formatter.parse(to_date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/createBooking", method = RequestMethod.POST)
	public Booking createBooking(@RequestBody @Valid final Booking booking) {
		return bookingService.save(booking);
	}

}
