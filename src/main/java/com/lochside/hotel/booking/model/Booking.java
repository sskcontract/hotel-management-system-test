package com.lochside.hotel.booking.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "BOOKINGS")
public class Booking implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6077509721985356641L;

	@Id
	Long bookingId;

	Long roomId;

	Long customerId;
	Date fromDate;
	Date toDate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "customerId", referencedColumnName = "CUSTOMER_ID", insertable = false, updatable = false)
	Customer customer;

	public Booking(Long bookingId, Room room, Customer customer, Date fromDate, Date toDate) {
		super();
		this.bookingId = bookingId;
		this.room = room;
		this.customer = customer;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public Booking() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "roomId", referencedColumnName = "ROOM_ID", insertable = false, updatable = false)
	Room room;

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Long getRoomId() {
		return this.room.getRoomId();
	}


	public Long getCustomerId() {
		return this.getCustomer().getCustomerId();
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
