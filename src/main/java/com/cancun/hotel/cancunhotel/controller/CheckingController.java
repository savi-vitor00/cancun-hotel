package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.VO.UnavailablePeriodsVO;
import com.cancun.hotel.cancunhotel.service.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkRoom")
public class CheckingController {

    private final CheckingService checkingService;

    @Autowired
    public CheckingController(CheckingService checkingService){
        this.checkingService = checkingService;
    }

    @GetMapping
    public ResponseEntity<UnavailablePeriodsVO> checkRoomAvailability(){
        UnavailablePeriodsVO unavailablePeriodsVO = checkingService.checkAvailability();
        return ResponseEntity.status(HttpStatus.OK).body(unavailablePeriodsVO);
    }

    @GetMapping("/byDates")
    public ResponseEntity checkRoomAvailabilityByDates(@RequestBody BookedRoomVO bookedRoomVO){
        Boolean available = checkingService.checkAvailabilityByDates(bookedRoomVO);
        ResponseEntity.BodyBuilder status = ResponseEntity.status(HttpStatus.OK);
        if(available){
            return status.body("No booking was found for the given period.");
        }
        return status.body("The room is already booked at given period.");
    }

    @GetMapping("/listByDates")
    public ResponseEntity listRoomAvailabilityByDates(@RequestBody BookedRoomVO bookedRoomVO){
        List<BookedRoomDTO> periodsFound = checkingService.listAvailabilityByDates(bookedRoomVO);
        ResponseEntity.BodyBuilder status = ResponseEntity.status(HttpStatus.OK);
        if(periodsFound.isEmpty()){
            return status.body("No booking was found for the given period.");
        }
        return status.body(periodsFound);
    }
}
