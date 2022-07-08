package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.service.InitializeService;
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
public class InitializeHotelController {

    private final InitializeService initializeService;

    @Autowired
    public InitializeHotelController(InitializeService initializeService){
        this.initializeService = initializeService;
    }

    @PostMapping
    public ResponseEntity<Object> initializeHotel(){
        if(!initializeService.verifyInitialization()){
            Map<String, List<Object>> hotelInitializeMap = initializeService.insertObjectsForTestingAPIPurpose();
            return ResponseEntity.status(HttpStatus.OK).body(hotelInitializeMap);
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Initializaton has already ocurred. Only one room is alowed.");
    }
}
