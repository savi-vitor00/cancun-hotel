package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.CustomerDTO;
import com.cancun.hotel.cancunhotel.service.CancelingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cancel")
@Api(tags = { "5 - Cancel bookings"})
public class CancelingController {

    private final CancelingService cancelingService;

    @Autowired
    public CancelingController(CancelingService cancelingService){
        this.cancelingService = cancelingService;
    }

    @DeleteMapping("/{bookedRoomId}")
    @ApiOperation(value = "${CancelingController.cancelBookedRoom.name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully canceled booking"),
            @ApiResponse(code = 404, message = "Not found exception")})
    public ResponseEntity cancelBookedRoom(@PathVariable("bookedRoomId") Long bookedRoomId){
        cancelingService.cancelBookedRoom(bookedRoomId);
        return ResponseEntity.status(HttpStatus.OK).body("Your booking was canceled successfully.");
    }

    @DeleteMapping("/allByCustomer/{customerId}")
    @ApiOperation(value = "${CancelingController.cancelAllBookedRoomByUser.name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully canceled bookings"),
            @ApiResponse(code = 404, message = "Not found exception")})
    public ResponseEntity cancelAllBookedRoomByUser(@PathVariable("customerId") Long customerId){
        CustomerDTO customerDTO = cancelingService.cancelAllBookedRoomByUser(customerId);
        return ResponseEntity.status(HttpStatus.OK).body("All bookings for " + customerDTO.getName() + " were successfully canceled.");
    }
}
