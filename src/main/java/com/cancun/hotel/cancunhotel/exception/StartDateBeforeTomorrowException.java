package com.cancun.hotel.cancunhotel.exception;

public class StartDateBeforeTomorrowException extends RuntimeException{

    public StartDateBeforeTomorrowException(String message){
        super(message);
    }
}

