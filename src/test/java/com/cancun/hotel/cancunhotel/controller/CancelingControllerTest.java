package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.exception.BookedRoomNotFoundException;
import com.cancun.hotel.cancunhotel.exception.CustomerNotFoundException;
import com.cancun.hotel.cancunhotel.exception.util.EnumCustomExceptionControl;
import com.cancun.hotel.cancunhotel.repository.BookedRoomRepository;
import com.cancun.hotel.cancunhotel.repository.CustomerRepository;
import com.cancun.hotel.cancunhotel.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class CancelingControllerTest {

    @Autowired
    private CancelingController cancelingController;

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
        Assertions.assertTrue(cancelingController != null);
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

    private BookedRoom initializeNewBookedRoom() {
        bookedRoomRepository.deleteAll();
        Customer customer = customerRepository.findById(1L).get();
        Room room = roomRepository.findById(1L).get();
        BookedRoom bookedRoom = new BookedRoom();
        bookedRoom.setRoom(room);
        bookedRoom.setCustomer(customer);
        bookedRoom.setStartDate(LocalDate.now());
        bookedRoom.setEndDate(LocalDate.now().plus(3, ChronoUnit.DAYS));
        return bookedRoomRepository.save(bookedRoom);
    }

    private void initialize2NewBookedRooms() {
        bookedRoomRepository.deleteAll();
        Customer customer = customerRepository.findById(1L).get();
        Room room = roomRepository.findById(1L).get();
        BookedRoom bookedRoom = new BookedRoom();
        bookedRoom.setRoom(room);
        bookedRoom.setCustomer(customer);
        bookedRoom.setStartDate(LocalDate.now());
        bookedRoom.setEndDate(LocalDate.now().plus(3, ChronoUnit.DAYS));
        bookedRoomRepository.save(bookedRoom);
        BookedRoom bookedRoom2 = new BookedRoom();
        bookedRoom2.setRoom(room);
        bookedRoom2.setCustomer(customer);
        bookedRoom2.setStartDate(LocalDate.now().plus(10, ChronoUnit.DAYS));
        bookedRoom2.setEndDate(LocalDate.now().plus(13, ChronoUnit.DAYS));
        bookedRoomRepository.save(bookedRoom2);
    }

    @Test
    public void test_cancelBooking_when_bookedRoomInvalid(){
        BookedRoomNotFoundException thrown = Assertions.assertThrows(BookedRoomNotFoundException.class, () -> {
            cancelingController.cancelBookedRoom(0L);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_cancelBooking_when_bookedRoomValid(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ResponseEntity responseEntity = cancelingController.cancelBookedRoom(bookedRoom.getId());
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody(), "Your booking was canceled successfully.");
    }

    @Test
    public void test_cancelBookingsForCustomer_when_customerInvalid(){
        CustomerNotFoundException thrown = Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            cancelingController.cancelAllBookedRoomByUser(0L);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.CUSTOMER_NOT_FOUND.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_cancelBookingsForCustomer_when_customerValid_and_noBookingFound(){
        bookedRoomRepository.deleteAll();
        BookedRoomNotFoundException thrown = Assertions.assertThrows(BookedRoomNotFoundException.class, () -> {
            cancelingController.cancelAllBookedRoomByUser(1L);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND_C.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_cancelBookingsForCustomer_when_customerValid_and_bookingsAvailableForCanceling(){
        initialize2NewBookedRooms();
        int size = bookedRoomRepository.findAll().size();
        Assertions.assertEquals(size, 2);
        ResponseEntity responseEntity = cancelingController.cancelAllBookedRoomByUser(1L);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody(), "All bookings for Jim Halpert were successfully canceled.");
        size = bookedRoomRepository.findAll().size();
        Assertions.assertEquals(size, 0);
    }
}