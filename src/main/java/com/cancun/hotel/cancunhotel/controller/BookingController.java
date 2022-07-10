package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.exception.BookedRoomNotFoundException;
import com.cancun.hotel.cancunhotel.service.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(tags = { "3 - Insert and list bookings"})
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @GetMapping
    @ApiOperation(value = "${BookingController.listAllBookings.name}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of bookings"),
            @ApiResponse(code = 404, message = "Booking not found exception", response = BookedRoomNotFoundException.class)
        })
    public ResponseEntity<List<BookedRoomDTO>> listAllBookings(){
        List<BookedRoomDTO> bookedRooms = bookingService.listAllBookings();
        return ResponseEntity.status(HttpStatus.OK).body(bookedRooms);
    }

    @GetMapping("/customer/{customerId}")
    @ApiOperation(value = "${BookingController.listBookingByCustomerId.name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of bookings"),
            @ApiResponse(code = 404, message = "Not found exception")})
    public ResponseEntity<List<BookedRoomDTO>> listBookingByCustomerId(@ApiParam(value = "${BookingController.listBookingByCustomerId.ApiParam.name}")
                                                                           @PathVariable("customerId") Long customerId){
        List<BookedRoomDTO> bookedRoomsDTO = bookingService.listBookingByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(bookedRoomsDTO);
    }

    @PostMapping
    @ApiOperation(value = "${BookingController.bookRoom.name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully inserted booking"),
            @ApiResponse(code = 404, message = "Not found exception"),
            @ApiResponse(code = 406, message = "Invalid parameters")})
    public ResponseEntity<BookedRoomDTO> bookRoom(@ApiParam(value = "${BookingController.bookRoom.ApiParam.name}")
                                                      @RequestBody BookedRoomVO vo){
        BookedRoomDTO bookedRoomDTO = bookingService.bookRoom(vo);
        return ResponseEntity.status(HttpStatus.OK).body(bookedRoomDTO);
    }
}
