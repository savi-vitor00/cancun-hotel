package com.cancun.hotel.cancunhotel.exception.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public enum EnumCustomExceptionControl {

    CUSTOMER_NOT_FOUND      ("CUSTOMER_NOT_FOUND", "Customer not found", "No customer was found for given id", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND          ("ROOM_NOT_FOUND", "Room not found", "No room was found for given id", HttpStatus.NOT_FOUND),
    BOOKED_ROOM_NOT_FOUND_C ("BOOKED_ROOM_NOT_FOUND_C", "Booking not found", "No booking was found for given customer", HttpStatus.NOT_FOUND),
    BOOKED_ROOM_NOT_FOUND   ("BOOKED_ROOM_NOT_FOUND", "Booking not found", "No booking was found", HttpStatus.NOT_FOUND),
    PARAMETERS_NOT_VALID    ("PARAMETERS_NOT_VALID", "Parameters not valid", "Null parameters are not allowed", HttpStatus.NOT_ACCEPTABLE),
    PERIOD_NOT_AVAILABLE    ("PERIOD_NOT_AVAILABLE", "Period not available", "Given date conflicts with another booking.", HttpStatus.NOT_ACCEPTABLE),
    THIRTY_DAYS_ADVANCE     ("THIRTY_DAYS_ADVANCE", "Advance booking out of range", "Booking with a 30 days advance is not permitted.", HttpStatus.NOT_ACCEPTABLE),
    THREE_PLUS_DAYS         ("THREE_PLUS_DAYS", "Booking range not permited", "Booking more than 3 days in a row is not permitted.", HttpStatus.NOT_ACCEPTABLE);

    private String errorCode;
    private String errorDescription;
    private String message;
    private HttpStatus status;

    EnumCustomExceptionControl(String errorCode, String errorDescription, String message, HttpStatus status){
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.message = message;
        this.status = status;
    }

    public static EnumCustomExceptionControl getByErrorCode(String errorCode){
        return EnumCustomExceptionControl.valueOf(errorCode);
    }
}
