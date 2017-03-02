package com.lochside.hotel.booking.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "rooms")
public class Room {
	@Id
	@Column(name = "room_id")
	Long roomId;
	String roomType;
	double price;

	@OneToMany(mappedBy = "room", targetEntity = Booking.class, fetch = FetchType.EAGER)
	@Transient
	@JsonIgnore
	Collection<Booking> bookings;

	public Room(Long roomId, String roomType, double price) {
		super();
		this.roomId = roomId;
		this.roomType = roomType;
		this.price = price;
	}

	public Room() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Collection<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(Collection<Booking> bookings) {
		this.bookings = bookings;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
