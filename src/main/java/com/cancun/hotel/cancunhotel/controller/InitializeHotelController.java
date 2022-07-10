package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.service.InitializeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/initializeHotel")
@Api(tags = { "1 - Initialize room and default customers"})
public class InitializeHotelController {

    private final InitializeService initializeService;

    @Autowired
    public InitializeHotelController(InitializeService initializeService){
        this.initializeService = initializeService;
    }

    @PostMapping
    @ApiOperation(value = "${InitializeHotelController.initializeHotel.name}", notes = "${InitializeHotelController.initializeHotel.description}")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Method Not Allowed - Initialization has already occurred"),
                    @ApiResponse(code = 200, message = "Successful initialization")})
    public ResponseEntity initializeHotel(){
        if(!initializeService.verifyInitialization()){
            Map<String, List<Object>> hotelInitializeMap = initializeService.insertObjectsForTestingAPIPurpose();
            return ResponseEntity.status(HttpStatus.OK).body(hotelInitializeMap);
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Initializaton has already ocurred. Only one room is alowed.");
    }
}
