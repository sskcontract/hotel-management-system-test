package com.lochside.hotel.booking.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.lochside.hotel.booking.HotelBookingSystemApplication;
import com.lochside.hotel.booking.RoomDetails;
import com.lochside.hotel.booking.model.Booking;
import com.lochside.hotel.booking.model.Customer;
import com.lochside.hotel.booking.model.Room;
import com.lochside.hotel.booking.repository.BookingRepository;
import com.lochside.hotel.booking.repository.CustomerRepository;
import com.lochside.hotel.booking.repository.RoomRepository;
import com.lochside.hotel.booking.service.HotelBookingService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = HotelBookingSystemApplication.class)
@WebAppConfiguration
public class HotelBookingControllerTests {

	@Mock
	private HotelBookingService bookingService;

	@InjectMocks
	private HotelBookingController bookingControllerTest;

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private RoomRepository roomRepository;

	private MockMvc mockMvc;

	private List<Booking> bookingList = new ArrayList<>();

	@Before
	public void setUp() throws ParseException {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(bookingControllerTest).build();
		Customer customer = new Customer(new Long(111), "Jhonny2", "Depp2", "JJ.sp@gg.com");
		Room room = new Room(new Long(112), "Single", 50.0);
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Booking booking = new Booking(new Long(222), room, customer, formatter.parse("2016-10-22"),
				formatter.parse("2016-10-25"));

		Booking booking1 = new Booking(new Long(434), room, customer, formatter.parse("2016-11-22"),
				formatter.parse("2016-11-25"));

		this.bookingList.add(booking);

		this.bookingList.add(booking1);

	}

	@Test
	public void test_create_booking_success() throws Exception {
		final Booking newBooking = stubServiceToReturnBooking();
		final Booking booking = new Booking();
		Booking returnedBooking = bookingControllerTest.createBooking(booking);
		verify(bookingService, times(1)).save(booking);
		assertEquals("Returned Booking should come from the service", newBooking, returnedBooking);
	}

	@Test
	public void test_bookings_by_customer_id_all_success() throws Exception {
		when(bookingService.findBookingByCustomerId(anyLong())).thenReturn(this.bookingList);
		mockMvc.perform(get("/bookingDetails/customer/111")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].customerId", is(111))).andExpect(jsonPath("$[0].roomId", is(112)));
		verify(bookingService, times(1)).findBookingByCustomerId(anyLong());
	}

	@Test
	public void test_bookings_by_room_id_all_success() throws Exception {
		when(bookingService.findBookingByRoomId(anyLong())).thenReturn(this.bookingList);
		mockMvc.perform(get("/bookingDetails/room/112")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[1].roomId", is(112))).andExpect(jsonPath("$[1].bookingId", is(434)));
		verify(bookingService, times(1)).findBookingByRoomId(anyLong());
	}

	@Test
	public void test_available_rooms_success() throws Exception {
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Date fromDate = formatter.parse("2016-10-22");
		Date toDate = formatter.parse("2016-10-25");
		RoomDetails roomDetails = new RoomDetails();
		roomDetails.setNoOfRooms(2);
		roomDetails.setRoomType("Single");
		roomDetails.setTotalCharge(200);
		List<RoomDetails> details = Arrays.asList(roomDetails);
		when(bookingService.findAvailableRooms(fromDate, toDate)).thenReturn(details);
		mockMvc.perform(get("/bookingDetails/availableRooms/2016-10-22/2016-10-25")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));				
		verify(bookingService, times(1)).findAvailableRooms(any(), any());
	}

	private Booking stubServiceToReturnBooking() {
		final Booking booking = new Booking();
		when(bookingService.save(any(Booking.class))).thenReturn(booking);
		return booking;
	}

}
