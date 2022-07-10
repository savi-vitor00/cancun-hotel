package com.cancun.hotel.cancunhotel.service;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.VO.UnavailablePeriodsVO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.repository.BookedRoomRepository;
import com.cancun.hotel.cancunhotel.util.DomainToDTOConverter;
import com.cancun.hotel.cancunhotel.util.ValidationControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckingService {

    private final BookedRoomRepository bookedRoomRepository;

    @Autowired
    public CheckingService(BookedRoomRepository bookedRoomRepository){
        this.bookedRoomRepository = bookedRoomRepository;
    }

    public UnavailablePeriodsVO checkAvailability(){
        List<BookedRoom> bookedRoomList = bookedRoomRepository.findAll();
        if(bookedRoomList.isEmpty()){
            return null;
        }
        UnavailablePeriodsVO unavailablePeriodsVO = new UnavailablePeriodsVO();
        unavailablePeriodsVO.setUnavailableDates(new HashMap<>());
        for (BookedRoom bookedRoom : bookedRoomList) {
            Map<String, LocalDate> unavailablePeriods = new HashMap<>();
            unavailablePeriods.put("Start date", bookedRoom.getStartDate());
            unavailablePeriods.put("End date", bookedRoom.getEndDate());
            unavailablePeriodsVO.getUnavailableDates().put(bookedRoom.getCustomer().getName(), unavailablePeriods);
        }
        return unavailablePeriodsVO;
    }

    public Boolean checkAvailabilityByDates(BookedRoomVO bookedRoomVO){
        ValidationControl.verifyStartAndEndDateNotNull(bookedRoomVO);
        Integer periodsFound = bookedRoomRepository.checkUnavailabilityByStartAndEndDates(bookedRoomVO.getStartDate(), bookedRoomVO.getEndDate());
        if(periodsFound > 0){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean checkAvailabilityByDates(LocalDate startDate, LocalDate endDate){
        ValidationControl.verifyStartAndEndDateNotNull(startDate, endDate);
        Integer periodsFound = bookedRoomRepository.checkUnavailabilityByStartAndEndDates(startDate, endDate);
        if(periodsFound > 0){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public List<BookedRoomDTO> listAvailabilityByDates(LocalDate startDate, LocalDate endDate){
        ValidationControl.verifyStartAndEndDateNotNull(startDate, endDate);
        List<BookedRoom> periodsFound = bookedRoomRepository.listUnavailabilityByStartAndEndDates(startDate, endDate);
        List<BookedRoomDTO> bookedRoomDTOS = new ArrayList<>();
        for (BookedRoom bookedRoom : periodsFound) {
            BookedRoomDTO bookedRoomDTO = (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
            bookedRoomDTOS.add(bookedRoomDTO);
        }
        return bookedRoomDTOS;
    }
}
