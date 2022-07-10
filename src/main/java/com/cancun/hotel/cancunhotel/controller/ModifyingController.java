package com.cancun.hotel.cancunhotel.controller;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.ModifyBookedRoomVO;
import com.cancun.hotel.cancunhotel.service.ModifyingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modify")
@Api(tags = { "4 - Modify bookings"})
public class ModifyingController {

    private final ModifyingService modifyingService;

    @Autowired
    public ModifyingController(ModifyingService modifyingService){
        this.modifyingService = modifyingService;
    }

    @PostMapping
    @ApiOperation(value = "${ModifyingController.modifyBookedRoom.name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully modified booking"),
            @ApiResponse(code = 404, message = "Not found exception"),
            @ApiResponse(code = 406, message = "Invalid parameters")})
    public ResponseEntity<BookedRoomDTO> modifyBookedRoom(@RequestBody ModifyBookedRoomVO modifyBookedRoomVO){
        BookedRoomDTO bookedRoomDTO = modifyingService.modifyBookedRoom(modifyBookedRoomVO);
        return ResponseEntity.status(HttpStatus.OK).body(bookedRoomDTO);
    }
}
