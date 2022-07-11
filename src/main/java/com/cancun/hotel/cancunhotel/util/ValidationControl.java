package com.cancun.hotel.cancunhotel.util;

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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class ValidationControl {

    private static final Integer MAX_LIMIT_ADVANCE_BOOKING = 30;

    public static void verifyRulesForBooking(Optional<Room> room, Optional<Customer> customer, BookedRoomVO bookedRoomVO, Boolean available) {
        LocalDate now = LocalDate.now();
        verifyStartDateValid(bookedRoomVO, now);
        verifyMaxDaysLimitOfStay(bookedRoomVO);
        verifyStartAndEndDateNotNull(bookedRoomVO);
        verifyAdvanceBookingLimit(bookedRoomVO, now);
        verifyRoomExistence(room);
        verifyCustomerExistence(customer);
        verifyAvailablePeriodsForBooking(available);
    }

    public static void verifyDomainsIdNotNull(BookedRoomVO bookedRoomVO){
        if(bookedRoomVO.getRoom_id() == null || bookedRoomVO.getCustomer_id() == null){
            throw new ParametersNotValidException(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
        }
    }

    public static void verifyBookedRoomIdNotNull(ModifyBookedRoomVO bookedRoomVO){
        if(bookedRoomVO.getBooked_room_id() == null){
            throw new ParametersNotValidException(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
        }
    }

    private static void verifyMaxDaysLimitOfStay(BookedRoomVO bookedRoomVO) {
        if(bookedRoomVO.getStartDate().until(bookedRoomVO.getEndDate(), ChronoUnit.DAYS) > 3){
            throw new ThreePlusDaysBookingException(EnumCustomExceptionControl.THREE_PLUS_DAYS.getErrorCode());
        }
    }

    private static void verifyAdvanceBookingLimit(BookedRoomVO bookedRoomVO, LocalDate now) {
        if(now.until(bookedRoomVO.getStartDate(), ChronoUnit.DAYS) > MAX_LIMIT_ADVANCE_BOOKING){
            throw new ThirtyDaysAdvanceBookingException(EnumCustomExceptionControl.THIRTY_DAYS_ADVANCE.getErrorCode());
        }
    }

    private static void verifyAvailablePeriodsForBooking(Boolean available) {
        if(!available){
            throw new PeriodNotAvailableException(EnumCustomExceptionControl.PERIOD_NOT_AVAILABLE.getErrorCode());
        }
    }

    public static void verifyCustomerExistence(Optional<Customer> customer) {
        if(!customer.isPresent()){
            throw new CustomerNotFoundException(EnumCustomExceptionControl.CUSTOMER_NOT_FOUND.getErrorCode());
        }
    }

    public static void verifyBookedRoomExistenceByCustomerId(List<BookedRoom> bookedRooms){
        if(bookedRooms.isEmpty()){
            throw new BookedRoomNotFoundException(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND_C.getErrorCode());
        }
    }

    public static void verifyBookedRoomsExistence(List<BookedRoom> bookedRooms){
        if(bookedRooms.isEmpty()){
            throw new BookedRoomNotFoundException(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND.getErrorCode());
        }
    }

    public static void verifyBookedRoomExistence(Optional<BookedRoom> bookedRoom){
        if(!bookedRoom.isPresent()){
            throw new BookedRoomNotFoundException(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND.getErrorCode());
        }
    }

    private static void verifyRoomExistence(Optional<Room> room) {
        if(!room.isPresent()){
            throw new RoomNotFoundException(EnumCustomExceptionControl.ROOM_NOT_FOUND.getErrorCode());
        }
    }

    public static void verifyStartAndEndDateNotNull(BookedRoomVO bookedRoomVO) {
        if(bookedRoomVO.getStartDate() == null || bookedRoomVO.getEndDate() == null){
            throw new ParametersNotValidException(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
        }
    }

    public static void verifyStartAndEndDateNotNull(LocalDate startDate, LocalDate endDate) {
        if(startDate == null || endDate == null){
            throw new ParametersNotValidException(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
        }
    }

    private static void verifyStartDateValid(BookedRoomVO bookedRoomVO, LocalDate now) {
        if(bookedRoomVO.getStartDate().isBefore(now.plus(1, ChronoUnit.DAYS))){
            throw new StartDateBeforeTomorrowException(EnumCustomExceptionControl.START_DATE_BEF_TOMOR.getErrorCode());
        }
    }
}
