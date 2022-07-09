package com.cancun.hotel.cancunhotel.exception;

public class BookedRoomNotFoundException extends RuntimeException{

    public BookedRoomNotFoundException(String message){
        super(message);
    }
}

