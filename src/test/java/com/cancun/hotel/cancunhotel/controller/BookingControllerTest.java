package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.exception.BookedRoomNotFoundException;
import com.cancun.hotel.cancunhotel.exception.util.EnumCustomExceptionControl;
import com.cancun.hotel.cancunhotel.repository.BookedRoomRepository;
import com.cancun.hotel.cancunhotel.repository.CustomerRepository;
import com.cancun.hotel.cancunhotel.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class BookingControllerTest {

    @Autowired
    private BookingController bookingController;

    @Autowired
    private InitializeHotelController initializeHotelController;

    @Autowired
    private BookedRoomRepository bookedRoomRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void contextLoads(){
        Assertions.assertTrue(bookingController != null);
        Assertions.assertTrue(bookedRoomRepository != null);
        Assertions.assertTrue(roomRepository != null);
        Assertions.assertTrue(customerRepository != null);
        Assertions.assertTrue(initializeHotelController != null);
    }

    @BeforeAll
    public void cleanAllDataAndInitializeHotel(){
        bookedRoomRepository.deleteAll();
        if(roomRepository.findAll().isEmpty() && roomRepository.findAll().isEmpty()){
            initializeHotelController.initializeHotel();
        }
    }

    private void initializeNewBookedRoom() {
        bookedRoomRepository.deleteAll();
        Customer customer = customerRepository.findById(1L).get();
        Room room = roomRepository.findById(1L).get();
        BookedRoom bookedRoom = new BookedRoom();
        bookedRoom.setRoom(room);
        bookedRoom.setCustomer(customer);
        bookedRoom.setStartDate(LocalDate.now());
        bookedRoom.setEndDate(LocalDate.now().plus(3, ChronoUnit.DAYS));
        bookedRoomRepository.save(bookedRoom);
    }

    @Test
    public void test_listAllBookings(){
        initializeNewBookedRoom();
        ResponseEntity<List<BookedRoomDTO>> listResponseEntity = bookingController.listAllBookings();
        Assertions.assertEquals(listResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void test_listAllBookingsByCustomerId(){
        initializeNewBookedRoom();
        ResponseEntity<List<BookedRoomDTO>> listResponseEntity = bookingController.listBookingByCustomerId(1L);
        Assertions.assertEquals(listResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void test_booking(){
        bookedRoomRepository.deleteAll();
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(1L);
        vo.setCustomer_id(1l);
        vo.setStartDate(LocalDate.now().plus(7, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(10, ChronoUnit.DAYS));
        ResponseEntity<BookedRoomDTO> bookedRoomDTOResponseEntity = bookingController.bookRoom(vo);
        Assertions.assertEquals(bookedRoomDTOResponseEntity.getStatusCode(), HttpStatus.OK);
    }
}
