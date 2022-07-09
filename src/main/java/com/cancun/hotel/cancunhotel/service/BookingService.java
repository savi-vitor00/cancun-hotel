package com.cancun.hotel.cancunhotel.service;

import com.cancun.hotel.cancunhotel.DTO.BookedRoomDTO;
import com.cancun.hotel.cancunhotel.VO.BookedRoomVO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.exception.BookedRoomNotFoundException;
import com.cancun.hotel.cancunhotel.exception.CustomerNotFoundException;
import com.cancun.hotel.cancunhotel.exception.ParametersNotValidException;
import com.cancun.hotel.cancunhotel.exception.util.EnumCustomExceptionControl;
import com.cancun.hotel.cancunhotel.exception.PeriodNotAvailableException;
import com.cancun.hotel.cancunhotel.exception.RoomNotFoundException;
import com.cancun.hotel.cancunhotel.exception.ThirtyDaysAdvanceBookingException;
import com.cancun.hotel.cancunhotel.exception.ThreePlusDaysBookingException;
import com.cancun.hotel.cancunhotel.repository.BookedRoomRepository;
import com.cancun.hotel.cancunhotel.repository.CustomerRepository;
import com.cancun.hotel.cancunhotel.repository.RoomRepository;
import com.cancun.hotel.cancunhotel.util.DomainToDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    public BookingService(BookedRoomRepository bookedRoomRepository, CustomerRepository customerRepository, RoomRepository roomRepository,
                            CheckingService checkingService){
        this.bookedRoomRepository = bookedRoomRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.checkingService = checkingService;
    }

    public List<BookedRoomDTO> listAllBookings(){
        List<BookedRoom> bookedRooms = bookedRoomRepository.findAll();
        if(bookedRooms.isEmpty()){
            throw new BookedRoomNotFoundException(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND.getErrorCode());
        }
        List<BookedRoomDTO> bookedRoomDTOS = new ArrayList<>();
        for (BookedRoom bookedRoom : bookedRooms) {
            BookedRoomDTO bookedRoomDTO = (BookedRoomDTO) DomainToDTOConverter.convertObjToDTO(bookedRoom, BookedRoomDTO.class);
            bookedRoomDTOS.add(bookedRoomDTO);
        }
        return bookedRoomDTOS;
    }

    public List<BookedRoomDTO> listBookingByCustomerId(final Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(!customer.isPresent()){
            throw new CustomerNotFoundException(EnumCustomExceptionControl.CUSTOMER_NOT_FOUND.getErrorCode());
        }
        List<BookedRoom> bookedRooms = bookedRoomRepository.findAllByCustomer(customer.get());
        if(bookedRooms.isEmpty()){
            throw new BookedRoomNotFoundException(EnumCustomExceptionControl.BOOKED_ROOM_NOT_FOUND_C.getErrorCode());
        }
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
        Optional<Room> room = roomRepository.findById(vo.getRoom_id());
        Optional<Customer> customer = customerRepository.findById(vo.getCustomer_id());
        verifyRulesForBooking(room, customer, vo);
        BookedRoom bookedRoom = new BookedRoom();
        bookedRoom.setRoom(room.get());
        bookedRoom.setCustomer(customer.get());
        bookedRoom.setStartDate(vo.getStartDate());
        bookedRoom.setEndDate(vo.getEndDate());
        bookedRoom = bookedRoomRepository.save(bookedRoom);
        return bookedRoom;
    }

    private void verifyRulesForBooking(Optional<Room> room, Optional<Customer> customer, BookedRoomVO bookedRoomVO) {
        if(bookedRoomVO.getStartDate() == null || bookedRoomVO.getEndDate() == null || bookedRoomVO.getRoom_id() == null
            || bookedRoomVO.getCustomer_id() == null){
            throw new ParametersNotValidException(EnumCustomExceptionControl.PARAMETERS_NOT_VALID.getErrorCode());
        }

        if(!room.isPresent()){
            throw new RoomNotFoundException(EnumCustomExceptionControl.ROOM_NOT_FOUND.getErrorCode());
        }
        if(!customer.isPresent()){
            throw new CustomerNotFoundException(EnumCustomExceptionControl.CUSTOMER_NOT_FOUND.getErrorCode());
        }

        Boolean available = checkingService.checkAvailabilityByDates(bookedRoomVO);
        if(!available){
            throw new PeriodNotAvailableException(EnumCustomExceptionControl.PERIOD_NOT_AVAILABLE.getErrorCode());
        }
        LocalDate now = LocalDate.now();
        if(now.until(bookedRoomVO.getStartDate(), ChronoUnit.DAYS) > 29){
            throw new ThirtyDaysAdvanceBookingException(EnumCustomExceptionControl.THIRTY_DAYS_ADVANCE.getErrorCode());
        }

        if(bookedRoomVO.getStartDate().until(bookedRoomVO.getEndDate(), ChronoUnit.DAYS) > 3){
            throw new ThreePlusDaysBookingException(EnumCustomExceptionControl.THREE_PLUS_DAYS.getErrorCode());
        }
    }
}
