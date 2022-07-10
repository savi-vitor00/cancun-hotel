package com.cancun.hotel.cancunhotel;

import com.cancun.hotel.cancunhotel.controller.BookingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CancunHotelApplicationTests {

	@Autowired
	private BookingController bookingController;

	@Test
	void contextLoads() {
	}

}
