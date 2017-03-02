package com.lochside.hotel.booking.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.lochside.hotel.booking.RoomDetails;
import com.lochside.hotel.booking.model.Booking;
import com.lochside.hotel.booking.model.Customer;
import com.lochside.hotel.booking.repository.BookingRepository;
import com.lochside.hotel.booking.repository.CustomerRepository;

@Service
public class HotelBookingServiceImpl implements HotelBookingService {
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	@Transactional
	public Booking save(Booking booking) {
		Customer customer = customerRepository.save(booking.getCustomer());
		booking.setCustomer(customer);
		return bookingRepository.save(booking);
	}

	@Override
	public Collection<Booking> findBookingByCustomerId(Long customerId) {
		return bookingRepository.findByCustomerId(customerId);
	}

	@Override
	public Collection<Booking> findBookingByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);

	}

	@Override
	public List<RoomDetails> findAvailableRooms(Date fromDate, Date toDate) {
		String FIND_AVAILABLE_ROOMS = "SELECT COUNT(*) as no_of_rooms,R.ROOM_TYPE,SUM(R.PRICE) as total_charge"
				+ " FROM ROOMS R WHERE NOT EXISTS (SELECT 'X' FROM BOOKINGS WHERE " + "FROM_DATE >= ?"
				+ " AND TO_DATE <= ?" + ") GROUP BY R.ROOM_TYPE, R.PRICE";
		Date args[] = { fromDate, toDate };
		return jdbcTemplate.query(FIND_AVAILABLE_ROOMS, args, new RowMapper<RoomDetails>() {
			@Override
			public RoomDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				RoomDetails details = new RoomDetails();
				details.setNoOfRooms(rs.getInt("no_of_rooms"));
				details.setRoomType(rs.getString("ROOM_TYPE"));
				details.setTotalCharge(rs.getDouble("total_charge"));
				return details;
			}

		});
	}

}
