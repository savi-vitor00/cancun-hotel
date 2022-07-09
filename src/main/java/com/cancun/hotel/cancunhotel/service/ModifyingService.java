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

import java.util.Optional;

@Service
public class ModifyingService {

    private final BookedRoomRepository bookedRoomRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final ValidationControlService validationControlService;

    @Autowired
    public ModifyingService(BookedRoomRepository bookedRoomRepository, CustomerRepository customerRepository, ValidationControlService validationControlService,
                                RoomRepository roomRepository){
        this.bookedRoomRepository = bookedRoomRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.validationControlService = validationControlService;
    }

    public BookedRoomDTO modifyBookedRoom(BookedRoomVO bookedRoomVO){
        validationControlService.verifyBookedRoomIdNotNull(bookedRoomVO);
        Optional<BookedRoom> bookedRoomOptional = bookedRoomRepository.findById(bookedRoomVO.getBooked_room_id());
        validationControlService.verifyBookedRoomExistence(bookedRoomOptional);
        BookedRoom bookedRoom = createBookedRoom(bookedRoomVO, bookedRoomOptional.get());
        bookedRoom = bookedRoomRepository.save(bookedRoom);
        return (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
    }

    private BookedRoom createBookedRoom(BookedRoomVO vo, BookedRoom bookedRoom) {
        validationControlService.verifyDomainsIdNotNull(vo);
        Optional<Room> room = roomRepository.findById(vo.getRoom_id());
        Optional<Customer> customer = customerRepository.findById(vo.getCustomer_id());
        validationControlService.verifyRulesForBooking(room, customer, vo);
        compareAndUpdateBookedRoom(bookedRoom, vo, customer);
        return bookedRoom;
    }

    private void compareAndUpdateBookedRoom(BookedRoom bookedRoom, BookedRoomVO bookedRoomVO, Optional<Customer> customer) {
        if(!bookedRoom.getStartDate().isEqual(bookedRoomVO.getStartDate())){
            bookedRoom.setStartDate(bookedRoomVO.getStartDate());
        }

        if(!bookedRoom.getEndDate().isEqual(bookedRoomVO.getEndDate())){
            bookedRoom.setEndDate(bookedRoomVO.getEndDate());
        }

        if(bookedRoom.getCustomer().getId() != bookedRoomVO.getCustomer_id()){
            bookedRoom.setCustomer(customer.get());
        }
    }
}
