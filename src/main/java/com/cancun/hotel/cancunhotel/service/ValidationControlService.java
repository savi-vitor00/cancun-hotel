package com.cancun.hotel.cancunhotel.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ValidationControlService {

    private final CheckingService checkingService;

    private final Integer MAX_LIMIT_ADVANCE_BOOKING = 29;

    @Autowired
    public ValidationControlService(CheckingService checkingService){
        this.checkingService = checkingService;
    }

    public void verifyRulesForBooking(Optional<Room> room, Optional<Customer> customer, BookedRoomVO bookedRoomVO) {
        LocalDate now = LocalDate.now();
        verifyStartAndEndDateNotNull(bookedRoomVO);
        verifyStartDateValid(bookedRoomVO, now);
        verifyAdvanceBookingLimit(bookedRoomVO, now);
        verifyMaxDaysLimitOfStay(bookedRoomVO);
        verifyRoomExistence(room);
        verifyCustomerExistence(customer);
        verifyAvailablePeriodsForBooking(bookedRoomVO);
    }

    public void verifyDomainsIdNotNull(BookedRoomVO bookedRoomVO){
        if(bookedRoomVO.getRoom_id() == null || bookedRoomVO.getCustomer_id() == null){
            throw new ParametersNotValidException(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
        }
    }

    public void verifyBookedRoomIdNotNull(BookedRoomVO bookedRoomVO){
        if(bookedRoomVO.getBooked_room_id() == null){
            throw new ParametersNotValidException(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
        }
    }

    private void verifyMaxDaysLimitOfStay(BookedRoomVO bookedRoomVO) {
        if(bookedRoomVO.getStartDate().until(bookedRoomVO.getEndDate(), ChronoUnit.DAYS) > 3){
            throw new ThreePlusDaysBookingException(EnumCustomExceptionControl.THREE_PLUS_DAYS.getErrorCode());
        }
    }

    private void verifyAdvanceBookingLimit(BookedRoomVO bookedRoomVO, LocalDate now) {
        if(now.until(bookedRoomVO.getStartDate(), ChronoUnit.DAYS) > MAX_LIMIT_ADVANCE_BOOKING){
            throw new ThirtyDaysAdvanceBookingException(EnumCustomExceptionControl.THIRTY_DAYS_ADVANCE.getErrorCode());
        }
    }

    private void verifyAvailablePeriodsForBooking(BookedRoomVO bookedRoomVO) {
        Boolean available = checkingService.checkAvailabilityByDates(bookedRoomVO);
        if(!available){
            throw new PeriodNotAvailableException(EnumCustomExceptionControl.PERIOD_NOT_AVAILABLE.getErrorCode());
        }
    }

    public void verifyCustomerExistence(Optional<Customer> customer) {
        if(!customer.isPresent()){
            throw new CustomerNotFoundException(EnumCustomExceptionControl.CUSTOMER_NOT_FOUND.getErrorCode());
        }
    }

    public void verifyBookedRoomExistenceByCustomerId(List<BookedRoom> bookedRooms){
        if(bookedRooms.isEmpty()){
            throw new BookedRoomNotFoundException(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND_C.getErrorCode());
        }
    }

    public void verifyBookedRoomsExistence(List<BookedRoom> bookedRooms){
        if(bookedRooms.isEmpty()){
            throw new BookedRoomNotFoundException(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND.getErrorCode());
        }
    }

    public void verifyBookedRoomExistence(Optional<BookedRoom> bookedRoom){
        if(!bookedRoom.isPresent()){
            throw new BookedRoomNotFoundException(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND.getErrorCode());
        }
    }

    private void verifyRoomExistence(Optional<Room> room) {
        if(!room.isPresent()){
            throw new RoomNotFoundException(EnumCustomExceptionControl.ROOM_NOT_FOUND.getErrorCode());
        }
    }

    public void verifyStartAndEndDateNotNull(BookedRoomVO bookedRoomVO) {
        if(bookedRoomVO.getStartDate() == null || bookedRoomVO.getEndDate() == null){
            throw new ParametersNotValidException(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
        }
    }

    private void verifyStartDateValid(BookedRoomVO bookedRoomVO, LocalDate now) {
        if(bookedRoomVO.getStartDate().isBefore(now.plus(1, ChronoUnit.DAYS))){
            throw new StartDateBeforeTomorrowException(EnumCustomExceptionControl.START_DATE_BEF_TOMOR.getErrorCode());
        }
    }
}
