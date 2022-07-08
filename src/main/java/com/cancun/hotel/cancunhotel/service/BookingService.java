package com.cancun.hotel.cancunhotel.service;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.DTO.RoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Costumer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.repository.BookedRoomRepository;
import com.cancun.hotel.cancunhotel.repository.CostumerRepository;
import com.cancun.hotel.cancunhotel.repository.RoomRepository;
import com.cancun.hotel.cancunhotel.util.DomainToDTOConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookedRoomRepository bookedRoomRepository;
    private final CostumerRepository costumerRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingService(BookedRoomRepository bookedRoomRepository, CostumerRepository costumerRepository, RoomRepository roomRepository){
        this.bookedRoomRepository = bookedRoomRepository;
        this.costumerRepository = costumerRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public List<BookedRoomDTO> listAllBookings(){
        List<BookedRoom> bookedRooms = bookedRoomRepository.findAll();
        List<BookedRoomDTO> bookedRoomDTOS = new ArrayList<>();
        for (BookedRoom bookedRoom : bookedRooms) {
            BookedRoomDTO bookedRoomDTO = (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
            bookedRoomDTOS.add(bookedRoomDTO);
        }
        return bookedRoomDTOS;
    }

    public List<BookedRoomDTO> listBookingByCostumerId(final Long costumerId){
        Costumer costumer = costumerRepository.findById(costumerId).get();
        List<BookedRoom> bookedRooms = bookedRoomRepository.findAllByCostumer(costumer);
        List<BookedRoomDTO> bookedRoomDTOS = new ArrayList<>();
        for (BookedRoom bookedRoom : bookedRooms) {
            BookedRoomDTO bookedRoomDTO = (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
            bookedRoomDTOS.add(bookedRoomDTO);
        }
        return bookedRoomDTOS;
    }

    public BookedRoomDTO bookRoom(BookedRoomVO vo) {
        BookedRoom bookedRoom = createBookedRoom(vo);
        BookedRoomDTO bookedRoomDTO = (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
        return bookedRoomDTO;
    }

    private BookedRoom createBookedRoom(BookedRoomVO vo) {
        Room room = roomRepository.findById(vo.getRoom_id()).get();
        Costumer costumer = costumerRepository.findById(vo.getCostumer_id()).get();
        BookedRoom bookedRoom = new BookedRoom();
        bookedRoom.setRoom(room);
        bookedRoom.setCostumer(costumer);
        bookedRoom.setStartDate(vo.getStartDate());
        bookedRoom.setEndDate(vo.getEndDate());
        bookedRoom = bookedRoomRepository.save(bookedRoom);
        return bookedRoom;
    }
}
