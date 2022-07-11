package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.VO.ModifyBookedRoomVO;
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
public class ModifyingControllerTest {

    @Autowired
    private ModifyingController modifyingController;

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
        Assertions.assertTrue(modifyingController != null);
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
        bookedRoom.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        bookedRoom.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        return bookedRoomRepository.save(bookedRoom);
    }


    @Test
    public void test_modifying_allValidationsCorrect(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(bookedRoom.getRoom().getId());
        Customer newCustomer = customerRepository.findById(2L).get();
        vo.setCustomer_id(newCustomer.getId());
        vo.setStartDate(bookedRoom.getStartDate().plus(10, ChronoUnit.DAYS));
        vo.setEndDate(bookedRoom.getEndDate().plus(10, ChronoUnit.DAYS));
        BookedRoomDTO bookedRoomDTO = modifyingController.modifyBookedRoom(vo).getBody();
        Assertions.assertEquals(bookedRoomDTO.getCustomer().getId(), newCustomer.getId());
        Assertions.assertEquals(bookedRoomDTO.getStartDate(), vo.getStartDate());
        Assertions.assertEquals(bookedRoomDTO.getEndDate(), vo.getEndDate());
    }

    @Test
    public void test_modifying_bookedRoomIdNull(){
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(null);
        vo.setRoom_id(1L);
        Customer newCustomer = customerRepository.findById(2L).get();
        vo.setCustomer_id(newCustomer.getId());
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));

        ParametersNotValidException thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(thrown.getMessage(), EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
    }

    @Test
    public void test_modifying_bookedRoomInvalid(){
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(0L);
        vo.setRoom_id(1L);
        Customer newCustomer = customerRepository.findById(2L).get();
        vo.setCustomer_id(newCustomer.getId());
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));

        BookedRoomNotFoundException thrown = Assertions.assertThrows(BookedRoomNotFoundException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(thrown.getMessage(), EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND.getErrorCode());
    }

    @Test
    public void test_modifying_when_domainsIdNull(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(null);
        vo.setCustomer_id(null);
        vo.setStartDate(LocalDate.now().plus(7, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(10, ChronoUnit.DAYS));
        ParametersNotValidException thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_modifying_when_starDateOrEndDateNull(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(null);
        vo.setEndDate(null);
        ParametersNotValidException thrown = Assertions.assertThrows(ParametersNotValidException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_modifying_when_startDateNotValid(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now());
        vo.setEndDate(LocalDate.now().plus(3, ChronoUnit.DAYS));
        StartDateBeforeTomorrowException thrown = Assertions.assertThrows(StartDateBeforeTomorrowException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.START_DATE_BEF_TOMOR.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_modifying_when_advanceBookingHigherThanThirtyDays(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now().plus(31, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(34, ChronoUnit.DAYS));
        ThirtyDaysAdvanceBookingException thrown = Assertions.assertThrows(ThirtyDaysAdvanceBookingException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.THIRTY_DAYS_ADVANCE.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_modifying_when_periodOfMaxDaysInvalid(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(5, ChronoUnit.DAYS));
        ThreePlusDaysBookingException thrown = Assertions.assertThrows(ThreePlusDaysBookingException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.THREE_PLUS_DAYS.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_modifying_when_roomDoesNotExist(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(0L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        RoomNotFoundException thrown = Assertions.assertThrows(RoomNotFoundException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.ROOM_NOT_FOUND.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_modifying_when_customerDoesNotExist(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(1L);
        vo.setCustomer_id(0L);
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        CustomerNotFoundException thrown = Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.CUSTOMER_NOT_FOUND.getErrorCode(), thrown.getMessage());
    }

    @Test
    public void test_modifying_when_periodNotAvailable(){
        BookedRoom bookedRoom = initializeNewBookedRoom();
        ModifyBookedRoomVO vo = new ModifyBookedRoomVO();
        vo.setBooked_room_id(bookedRoom.getId());
        vo.setRoom_id(1L);
        vo.setCustomer_id(1L);
        vo.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        vo.setEndDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        PeriodNotAvailableException thrown = Assertions.assertThrows(PeriodNotAvailableException.class, () -> {
            modifyingController.modifyBookedRoom(vo);
        });
        Assertions.assertEquals(EnumCustomExceptionControl.PERIOD_NOT_AVAILABLE.getErrorCode(), thrown.getMessage());
    }
}
