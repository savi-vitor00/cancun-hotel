package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.CustomerDTO;
import com.cancun.hotel.cancunhotel.service.CancelingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cancel")
public class CancelingController {

    private final CancelingService cancelingService;

    @Autowired
    public CancelingController(CancelingService cancelingService){
        this.cancelingService = cancelingService;
    }

    @DeleteMapping("/{bookedRoomId}")
    public ResponseEntity cancelBookedRoom(@PathVariable("bookedRoomId") Long bookedRoomId){
        cancelingService.cancelBookedRoom(bookedRoomId);
        return ResponseEntity.status(HttpStatus.OK).body("Your booking was sucessfully canceled.");
    }

    @DeleteMapping("/allByCustomer/{customerId}")
    public ResponseEntity cancelAllBookedRoomByUser(@PathVariable("customerId") Long customerId){
        CustomerDTO customerDTO = cancelingService.cancelAllBookedRoomByUser(customerId);
        return ResponseEntity.status(HttpStatus.OK).body("All bookings for " + customerDTO.getName() + " were sucessfully canceled.");
    }
}
