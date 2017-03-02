package com.lochside.hotel.booking.exception;

public class BookingNotFoundException extends RuntimeException {

	public BookingNotFoundException(String id) {
		super("could not find id '" + id + "'.");
	}
}
