package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookedRoomDTO>> listAllBookings(){
        List<BookedRoomDTO> bookedRooms = bookingService.listAllBookings();
        return ResponseEntity.status(HttpStatus.OK).body(bookedRooms);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookedRoomDTO>> listBookingByCustomerId(@PathVariable("customerId") Long customerId){
        List<BookedRoomDTO> bookedRoomsDTO = bookingService.listBookingByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(bookedRoomsDTO);
    }

    @PostMapping
    public ResponseEntity<BookedRoomDTO> bookRoom(@RequestBody BookedRoomVO vo){
        BookedRoomDTO bookedRoomDTO = bookingService.bookRoom(vo);
        return ResponseEntity.status(HttpStatus.OK).body(bookedRoomDTO);
    }
}
