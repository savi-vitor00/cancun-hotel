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
import com.cancun.hotel.cancunhotel.util.ValidationControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookedRoomRepository bookedRoomRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final CheckingService checkingService;

    @Autowired
    public BookingService(BookedRoomRepository bookedRoomRepository, CustomerRepository customerRepository, RoomRepository roomRepository, CheckingService checkingService){
        this.bookedRoomRepository = bookedRoomRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.checkingService = checkingService;
    }

    public List<BookedRoomDTO> listAllBookings(){
        List<BookedRoom> bookedRooms = bookedRoomRepository.findAll();
        ValidationControl.verifyBookedRoomsExistence(bookedRooms);
        List<BookedRoomDTO> bookedRoomDTOS = new ArrayList<>();
        for (BookedRoom bookedRoom : bookedRooms) {
            BookedRoomDTO bookedRoomDTO = (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
            bookedRoomDTOS.add(bookedRoomDTO);
        }
        return bookedRoomDTOS;
    }

    public List<BookedRoomDTO> listBookingByCustomerId(final Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);
        ValidationControl.verifyCustomerExistence(customer);
        List<BookedRoom> bookedRooms = bookedRoomRepository.findAllByCustomer(customer.get());
        ValidationControl.verifyBookedRoomExistenceByCustomerId(bookedRooms);
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
        ValidationControl.verifyDomainsIdNotNull(vo);
        Optional<Room> room = roomRepository.findById(vo.getRoom_id());
        Optional<Customer> customer = customerRepository.findById(vo.getCustomer_id());
        Boolean available = checkingService.checkAvailabilityByDates(vo);
        ValidationControl.verifyRulesForBooking(room, customer, vo, available);
        BookedRoom bookedRoom = buildBookedRoomObject(vo, room, customer);
        return bookedRoomRepository.save(bookedRoom);
    }

    private BookedRoom buildBookedRoomObject(BookedRoomVO vo, Optional<Room> room, Optional<Customer> customer) {
        BookedRoom bookedRoom = new BookedRoom();
        bookedRoom.setRoom(room.get());
        bookedRoom.setCustomer(customer.get());
        bookedRoom.setStartDate(vo.getStartDate());
        bookedRoom.setEndDate(vo.getEndDate());
        return bookedRoom;
    }
}
