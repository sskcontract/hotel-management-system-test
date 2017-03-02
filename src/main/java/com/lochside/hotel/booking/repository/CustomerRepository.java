package com.lochside.hotel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lochside.hotel.booking.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
