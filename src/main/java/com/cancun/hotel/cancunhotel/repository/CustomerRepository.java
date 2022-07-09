package com.cancun.hotel.cancunhotel.repository;

import com.cancun.hotel.cancunhotel.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
