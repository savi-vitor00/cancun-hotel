package com.cancun.hotel.cancunhotel.service;

import com.cancun.hotel.cancunhotel.DTO.CostumerDTO;
import com.cancun.hotel.cancunhotel.DTO.RoomDTO;
import com.cancun.hotel.cancunhotel.domain.Costumer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.repository.CostumerRepository;
import com.cancun.hotel.cancunhotel.repository.RoomRepository;
import com.cancun.hotel.cancunhotel.util.DomainToDTOConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InitializeService {

    private final CostumerRepository costumerRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public InitializeService(CostumerRepository costumerRepository, RoomRepository roomRepository){
        this.costumerRepository = costumerRepository;
        this.roomRepository = roomRepository;
    }

    public Map<String, List<Object>> insertObjectsForTestingAPIPurpose() {
        Map<String, List<Object>> hotelInitializeMap = new HashMap<String, List<Object>>();
        RoomDTO room = initializeRoom();
        List<CostumerDTO> costumerList = createCostumers();
        hotelInitializeMap.put("Created Room", Arrays.asList(room));
        hotelInitializeMap.put("Created Costumers", Arrays.asList(costumerList));
        return hotelInitializeMap;
    }

    private List<CostumerDTO> createCostumers() {
        List<CostumerDTO> costumerList = new ArrayList<>();
        costumerList.add(initializeCostumer("Jim Halpert"));
        costumerList.add(initializeCostumer("John Winchester"));
        costumerList.add(initializeCostumer("Dustin Henderson"));
        return costumerList;
    }

    public RoomDTO initializeRoom(){
        Room room = new Room();
        room.setRoomNumber(1);
        room.setDescription("Master Bedroom");
        room = roomRepository.save(room);
        return (RoomDTO) DomainToDTOConverter.convertObjToDTO(room, RoomDTO.class);
    }

    public CostumerDTO initializeCostumer(final String name){
        Costumer costumer = new Costumer();
        costumer.setName(name);
        costumer = costumerRepository.save(costumer);
        return (CostumerDTO) DomainToDTOConverter.convertObjToDTO(costumer, CostumerDTO.class);
    }

    public Boolean verifyInitialization(){
        List<Room> rooms = roomRepository.findAll();
        if(rooms != null && rooms.size() > 0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
