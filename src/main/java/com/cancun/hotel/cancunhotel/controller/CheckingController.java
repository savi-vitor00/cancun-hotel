package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.UnavailablePeriodsVO;
import com.cancun.hotel.cancunhotel.service.CheckingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/checkRoom")
@Api(tags = { "2 - Check available dates for booking in"})
public class CheckingController {

    private final CheckingService checkingService;

    @Autowired
    public CheckingController(CheckingService checkingService){
        this.checkingService = checkingService;
    }

    @GetMapping
    @ApiOperation(value = "${CheckingController.checkRoomAvailability.name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully listed periods"),
            @ApiResponse(code = 406, message = "Invalid parameters"),
            @ApiResponse(code = 400, message = "Invalid format date")})
    public ResponseEntity checkRoomAvailability(){
        UnavailablePeriodsVO unavailablePeriodsVO = checkingService.checkAvailability();
        if(unavailablePeriodsVO == null){
            return ResponseEntity.status(HttpStatus.OK).body("No booking was found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(unavailablePeriodsVO);
    }

    @GetMapping("/byDates")
    @ApiOperation(value = "${CheckingController.checkRoomAvailabilityByDates.name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully checked periods"),
            @ApiResponse(code = 406, message = "Invalid parameters"),
            @ApiResponse(code = 400, message = "Invalid format date")})
    public ResponseEntity checkRoomAvailabilityByDates(@ApiParam("Start Date for booking, pattern = 'yyyy/MM/dd'") @RequestParam("startDate")
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy/MM/dd") LocalDate startDate,
                                                       @ApiParam("Start Date for booking, pattern = 'yyyy/MM/dd'") @RequestParam("endDate")
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy/MM/dd") LocalDate endDate){
        Boolean available = checkingService.checkAvailabilityByDates(startDate, endDate);
        ResponseEntity.BodyBuilder status = ResponseEntity.status(HttpStatus.OK);
        if(available){
            return status.body("No booking was found for the given period.");
        }
        return status.body("The room is already booked at given period.");
    }

    @GetMapping("/listByDates")
    @ApiOperation(value = "${CheckingController.listRoomAvailabilityByDates.name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully listed periods"),
            @ApiResponse(code = 406, message = "Invalid parameters"),
            @ApiResponse(code = 400, message = "Invalid format date")})
    public ResponseEntity listRoomAvailabilityByDates(@ApiParam("Start Date for booking, pattern = 'yyyy/MM/dd'") @RequestParam("startDate")
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy/MM/dd") LocalDate startDate,
                                                      @ApiParam("Start Date for booking, pattern = 'yyyy/MM/dd'") @RequestParam("endDate")
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy/MM/dd") LocalDate endDate){
        List<BookedRoomDTO> periodsFound = checkingService.listAvailabilityByDates(startDate, endDate);
        ResponseEntity.BodyBuilder status = ResponseEntity.status(HttpStatus.OK);
        if(periodsFound.isEmpty()){
            return status.body("No booking was found for the given period.");
        }
        return status.body(periodsFound);
    }
}
