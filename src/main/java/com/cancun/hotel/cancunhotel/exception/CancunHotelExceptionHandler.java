package com.cancun.hotel.cancunhotel.exception;

import com.cancun.hotel.cancunhotel.exception.util.ApiErrorObject;
import com.cancun.hotel.cancunhotel.exception.util.EnumCustomExceptionControl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CancunHotelExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ThreePlusDaysBookingException.class)
    protected ResponseEntity handleThreePlusDaysBookingException(ThreePlusDaysBookingException ex) {
        ApiErrorObject apiError = getApiErrorObject(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ThirtyDaysAdvanceBookingException.class)
    protected ResponseEntity handleThirtyDaysAdvanceBookingException(ThirtyDaysAdvanceBookingException ex) {
        ApiErrorObject apiError = getApiErrorObject(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    protected ResponseEntity handleRoomNotFoundException(RoomNotFoundException ex) {
        ApiErrorObject apiError = getApiErrorObject(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    protected ResponseEntity handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ApiErrorObject apiError = getApiErrorObject(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(PeriodNotAvailableException.class)
    protected ResponseEntity handlePeriodNotAvailableException(PeriodNotAvailableException ex) {
        ApiErrorObject apiError = getApiErrorObject(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ParametersNotValidException.class)
    protected ResponseEntity handleParametersNotValidException(ParametersNotValidException ex) {
        ApiErrorObject apiError = getApiErrorObject(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BookedRoomNotFoundException.class)
    protected ResponseEntity handleBookedRoomNotFoundException(BookedRoomNotFoundException ex) {
        ApiErrorObject apiError = getApiErrorObject(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(StartDateBeforeTomorrowException.class)
    protected ResponseEntity handleStartDateBeforeTomorrowException(StartDateBeforeTomorrowException ex) {
        ApiErrorObject apiError = getApiErrorObject(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ApiErrorObject getApiErrorObject(String errorCode) {
        EnumCustomExceptionControl errorControl = EnumCustomExceptionControl.getByErrorCode(errorCode);
        ApiErrorObject apiError = new ApiErrorObject();
        apiError.setStatus(errorControl.getStatus());
        apiError.setStatusId(errorControl.getStatus().value());
        apiError.setMessage(errorControl.getMessage());
        apiError.setError(errorControl.getErrorDescription());
        return apiError;
    }

    private ResponseEntity buildResponseEntity(ApiErrorObject apiError) {
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
