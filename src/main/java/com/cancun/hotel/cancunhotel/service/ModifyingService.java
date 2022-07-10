package com.cancun.hotel.cancunhotel.service;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.VO.ModifyBookedRoomVO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.repository.BookedRoomRepository;
import com.cancun.hotel.cancunhotel.repository.CustomerRepository;
import com.cancun.hotel.cancunhotel.repository.RoomRepository;
import com.cancun.hotel.cancunhotel.util.DomainToDTOConverter;
import com.cancun.hotel.cancunhotel.util.ValidationControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModifyingService {

    private final BookedRoomRepository bookedRoomRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final CheckingService checkingService;

    @Autowired
    public ModifyingService(BookedRoomRepository bookedRoomRepository, CustomerRepository customerRepository,
                                RoomRepository roomRepository, CheckingService checkingService){
        this.bookedRoomRepository = bookedRoomRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.checkingService = checkingService;
    }

    public BookedRoomDTO modifyBookedRoom(ModifyBookedRoomVO modifyBookedRoomVO){
        ValidationControl.verifyBookedRoomIdNotNull(modifyBookedRoomVO);
        Optional<BookedRoom> bookedRoomOptional = bookedRoomRepository.findById(modifyBookedRoomVO.getBooked_room_id());
        ValidationControl.verifyBookedRoomExistence(bookedRoomOptional);
        BookedRoomVO bookedRoomVO = new BookedRoomVO(modifyBookedRoomVO.getRoom_id(), modifyBookedRoomVO.getCustomer_id(), modifyBookedRoomVO.getStartDate(), modifyBookedRoomVO.getEndDate());
        BookedRoom bookedRoom = createBookedRoom(bookedRoomVO, bookedRoomOptional.get());
        bookedRoom = bookedRoomRepository.save(bookedRoom);
        return (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
    }

    private BookedRoom createBookedRoom(BookedRoomVO vo, BookedRoom bookedRoom) {
        ValidationControl.verifyDomainsIdNotNull(vo);
        Optional<Room> room = roomRepository.findById(vo.getRoom_id());
        Optional<Customer> customer = customerRepository.findById(vo.getCustomer_id());
        Boolean available = checkingService.checkAvailabilityByDates(vo);
        ValidationControl.verifyRulesForBooking(room, customer, vo, available);
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
