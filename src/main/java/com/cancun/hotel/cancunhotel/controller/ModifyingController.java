package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.service.ModifyingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modify")
public class ModifyingController {

    private final ModifyingService modifyingService;

    @Autowired
    public ModifyingController(ModifyingService modifyingService){
        this.modifyingService = modifyingService;
    }

    @PostMapping
    public ResponseEntity<BookedRoomDTO> modifyBookedRoom(@RequestBody BookedRoomVO bookedRoomVO){
        BookedRoomDTO bookedRoomDTO = modifyingService.modifyBookedRoom(bookedRoomVO);
        return ResponseEntity.status(HttpStatus.OK).body(bookedRoomDTO);
    }
}
