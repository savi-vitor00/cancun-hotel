package com.cancun.hotel.cancunhotel.exception;

public class RoomNotFoundException extends RuntimeException{

    public RoomNotFoundException(String message){
        super(message);
    }
}

