package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.UnavailablePeriodsVO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.exception.BookedRoomNotFoundException;
import com.cancun.hotel.cancunhotel.exception.CustomerNotFoundException;
import com.cancun.hotel.cancunhotel.exception.ParametersNotValidException;
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
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class CheckingControllerTest {

    @Autowired
    private CheckingController checkingController;

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
        Assertions.assertTrue(checkingController != null);
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
    public void test_checkRoomAvailability_when_noBookingExists(){
        bookedRoomRepository.deleteAll();
        ResponseEntity responseEntity = checkingController.checkRoomAvailability();
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody(), "No booking was found.");
    }

    @Test
    public void test_checkRoomAvailability_when_bookingExists(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ResponseEntity responseEntity = checkingController.checkRoomAvailability();
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        UnavailablePeriodsVO periods = (UnavailablePeriodsVO) responseEntity.getBody();
        Assertions.assertEquals(periods.getUnavailableDates().size(), 1);
        Map<String, LocalDate> periodsBooked = periods.getUnavailableDates().get("Jim Halpert");
        Assertions.assertTrue(periodsBooked != null);
        Assertions.assertEquals(periodsBooked.get("Start date"), bookedRoom.getStartDate());
        Assertions.assertEquals(periodsBooked.get("End date"), bookedRoom.getEndDate());
    }

    @Test
    public void test_checkRoomAvailabilityByDates_when_parametersNotValid(){
        ParametersNotValidException thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
           checkingController.checkRoomAvailabilityByDates(null, null);
        });
        Assertions.assertEquals(thrown.getMessage(), EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());

        thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            checkingController.checkRoomAvailabilityByDates(null, LocalDate.now());
        });
        Assertions.assertEquals(thrown.getMessage(), EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());

        thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            checkingController.checkRoomAvailabilityByDates(LocalDate.now(), null);
        });
        Assertions.assertEquals(thrown.getMessage(), EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
    }

    @Test
    public void test_checkRoomAvailabilityByDates_when_noBookingsExists(){
        bookedRoomRepository.deleteAll();
        ResponseEntity responseEntity = checkingController.checkRoomAvailabilityByDates(LocalDate.now(), LocalDate.now().plus(3, ChronoUnit.DAYS));
        Assertions.assertEquals(responseEntity.getBody(), "No booking was found for the given period.");
    }

    @Test
    public void test_checkRoomAvailabilityByDates_when_bookingsExists(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ResponseEntity responseEntity = checkingController.checkRoomAvailabilityByDates(bookedRoom.getStartDate(), bookedRoom.getEndDate());
        Assertions.assertEquals(responseEntity.getBody(), "The room is already booked at given period.");
    }

    @Test
    public void test_listRoomAvailabilityByDates_when_parametersNotValid(){
        ParametersNotValidException thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            checkingController.listRoomAvailabilityByDates(null, null);
        });
        Assertions.assertEquals(thrown.getMessage(), EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());

        thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            checkingController.listRoomAvailabilityByDates(null, LocalDate.now());
        });
        Assertions.assertEquals(thrown.getMessage(), EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());

        thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            checkingController.listRoomAvailabilityByDates(LocalDate.now(), null);
        });
        Assertions.assertEquals(thrown.getMessage(), EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
    }

    @Test
    public void test_listRoomAvailabilityByDates_when_noBookingsExists(){
        bookedRoomRepository.deleteAll();
        ResponseEntity responseEntity = checkingController.listRoomAvailabilityByDates(LocalDate.now(), LocalDate.now().plus(3, ChronoUnit.DAYS));
        Assertions.assertEquals(responseEntity.getBody(), "No booking was found for the given period.");
    }

    @Test
    public void test_listRoomAvailabilityByDates_when_bookingsExists(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ResponseEntity responseEntity = checkingController.listRoomAvailabilityByDates(bookedRoom.getStartDate(), bookedRoom.getEndDate());
        List<BookedRoomDTO> bookings = (List<BookedRoomDTO>) responseEntity.getBody();
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.iterator().next().getCustomer().getId(), bookedRoom.getCustomer().getId());
        Assertions.assertEquals(bookings.iterator().next().getId(), bookedRoom.getId());
    }

}