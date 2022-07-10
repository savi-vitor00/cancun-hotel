package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.exception.BookedRoomNotFoundException;
import com.cancun.hotel.cancunhotel.exception.CustomerNotFoundException;
import com.cancun.hotel.cancunhotel.exception.ParametersNotValidException;
import com.cancun.hotel.cancunhotel.exception.PeriodNotAvailableException;
import com.cancun.hotel.cancunhotel.exception.RoomNotFoundException;
import com.cancun.hotel.cancunhotel.exception.StartDateBeforeTomorrowException;
import com.cancun.hotel.cancunhotel.exception.ThirtyDaysAdvanceBookingException;
import com.cancun.hotel.cancunhotel.exception.ThreePlusDaysBookingException;
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
    public void test_listAllBookings_whenBookingExists(){
        initializeNewBookedRoom();
        ResponseEntity<List<BookedRoomDTO>> bookedRoomDTOS = bookingController.listAllBookings();
        Assertions.assertTrue(!bookedRoomDTOS.getBody().isEmpty());
    }

    @Test
    public void test_listAllBookings_whenNoBookingWasMade(){
        bookedRoomRepository.deleteAll();
        BookedRoomNotFoundException thrown = Assertions.assertThrows(BookedRoomNotFoundException.class, () -> {
            bookingController.listAllBookings();
        });
        Assertions.assertEquals(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_listByCustomerId_when_customerIdDoesNotExists(){
        CustomerNotFoundException thrown = Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            bookingController.listBookingByCustomerId(0L);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.CUSTOMER_NOT_FOUND.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_listByCustomerId_when_bookedRoomDoesNotExists_and_customerValid(){
        bookedRoomRepository.deleteAll();
        BookedRoomNotFoundException thrown = Assertions.assertThrows(BookedRoomNotFoundException.class, () -> {
            bookingController.listBookingByCustomerId(1L);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND_C.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_listByCustomerId_when_bookedRoomDoesExists_and_customerValid(){
        initializeNewBookedRoom();
        ResponseEntity<List<BookedRoomDTO>> bookedRoomDTOS = bookingController.listBookingByCustomerId(1L);
        Assertions.assertTrue(!bookedRoomDTOS.getBody().isEmpty());
    }

    @Test
    public void test_booking_allValidationsCorrect(){
        bookedRoomRepository.deleteAll();
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(1L);
        vo.setCustomer_id(1l);
        vo.setStartDate(LocalDate.now().plus(7, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(10, ChronoUnit.DAYS));
        ResponseEntity<BookedRoomDTO> bookedRoomDTO = bookingController.bookRoom(vo);
        Assertions.assertTrue(bookedRoomDTO.getBody().getId() != null);
    }

    @Test
    public void test_booking_when_domainsIdNull(){
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(null);
        vo.setCustomer_id(null);
        vo.setStartDate(LocalDate.now().plus(7, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(10, ChronoUnit.DAYS));
        ParametersNotValidException thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            bookingController.bookRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_booking_when_starDateOrEndDateNull(){
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(null);
        vo.setEndDate(null);
        ParametersNotValidException thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            bookingController.bookRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_booking_when_startDateNotValid(){
        bookedRoomRepository.deleteAll();
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now());
        vo.setEndDate(LocalDate.now().plus(3, ChronoUnit.DAYS));
        StartDateBeforeTomorrowException thrown = Assertions.assertThrows(StartDateBeforeTomorrowException.class, () -> {
            bookingController.bookRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.START_DATE_BEF_TOMOR.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_booking_when_advanceBookingHigherThanThirtyDays(){
        bookedRoomRepository.deleteAll();
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now().plus(31, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(34, ChronoUnit.DAYS));
        ThirtyDaysAdvanceBookingException thrown = Assertions.assertThrows(ThirtyDaysAdvanceBookingException.class, () -> {
            bookingController.bookRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.THIRTY_DAYS_ADVANCE.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_booking_when_periodOfMaxDaysInvalid(){
        bookedRoomRepository.deleteAll();
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(5, ChronoUnit.DAYS));
        ThreePlusDaysBookingException thrown = Assertions.assertThrows(ThreePlusDaysBookingException.class, () -> {
            bookingController.bookRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.THREE_PLUS_DAYS.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_booking_when_roomDoesNotExist(){
        bookedRoomRepository.deleteAll();
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(0L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        RoomNotFoundException thrown = Assertions.assertThrows(RoomNotFoundException.class, () -> {
            bookingController.bookRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.ROOM_NOT_FOUND.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_booking_when_customerDoesNotExist(){
        bookedRoomRepository.deleteAll();
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(1L);
        vo.setCustomer_id(0L);
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        CustomerNotFoundException thrown = Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            bookingController.bookRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.CUSTOMER_NOT_FOUND.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_booking_when_periodNotValid(){
        initializeNewBookedRoom();
        BookedRoomVO vo = new BookedRoomVO();
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        PeriodNotAvailableException thrown = Assertions.assertThrows(PeriodNotAvailableException.class, () -> {
            bookingController.bookRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.PERIOD_NOT_AVAILABLE.getErrorCode(), thrown.getMessage());
    }
}
