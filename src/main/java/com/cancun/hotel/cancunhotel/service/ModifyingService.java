package com.cancun.hotel.cancunhotel.service;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.repository.BookedRoomRepository;
import com.cancun.hotel.cancunhotel.repository.CustomerRepository;
import com.cancun.hotel.cancunhotel.repository.RoomRepository;
import com.cancun.hotel.cancunhotel.util.DomainToDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifyingService {

    private final BookedRoomRepository bookedRoomRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ModifyingService(BookedRoomRepository bookedRoomRepository, CustomerRepository customerRepository){
        this.bookedRoomRepository = bookedRoomRepository;
        this.customerRepository = customerRepository;
    }

    public BookedRoomDTO modifyBookedRoom(BookedRoomVO bookedRoomVO){
        BookedRoom bookedRoom = bookedRoomRepository.findById(bookedRoomVO.getBooked_room_id()).get();
        compareAndUpdateBookedRoom(bookedRoom, bookedRoomVO);
        bookedRoom = bookedRoomRepository.save(bookedRoom);
        return (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
    }

    private void compareAndUpdateBookedRoom(BookedRoom bookedRoom, BookedRoomVO bookedRoomVO) {
        if(!bookedRoom.getStartDate().isEqual(bookedRoomVO.getStartDate())){
            bookedRoom.setStartDate(bookedRoomVO.getStartDate());
        }

        if(!bookedRoom.getEndDate().isEqual(bookedRoomVO.getEndDate())){
            bookedRoom.setEndDate(bookedRoomVO.getEndDate());
        }

        if(bookedRoom.getCustomer().getId() != bookedRoomVO.getCustomer_id()){
            Customer customer = customerRepository.findById(bookedRoomVO.getCustomer_id()).get();
            bookedRoom.setCustomer(customer);
        }
    }
}
