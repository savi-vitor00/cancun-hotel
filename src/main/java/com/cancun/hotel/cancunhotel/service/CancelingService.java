package com.cancun.hotel.cancunhotel.service;

import com.cancun.hotel.cancunhotel.DTO.CustomerDTO;
import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.repository.BookedRoomRepository;
import com.cancun.hotel.cancunhotel.repository.CustomerRepository;
import com.cancun.hotel.cancunhotel.util.DomainToDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CancelingService {

    private final BookedRoomRepository bookedRoomRepository;
    private final CustomerRepository customerRepository;
    private final ValidationControlService validationControlService;

    @Autowired
    public CancelingService(BookedRoomRepository bookedRoomRepository, CustomerRepository customerRepository, ValidationControlService validationControlService){
        this.bookedRoomRepository = bookedRoomRepository;
        this.customerRepository = customerRepository;
        this.validationControlService = validationControlService;
    }

    public CustomerDTO cancelAllBookedRoomByUser(final Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);
        validationControlService.verifyCustomerExistence(customer);
        List<BookedRoom> bookedRoomsByCustomer = bookedRoomRepository.findAllByCustomer(customer.get());
        validationControlService.verifyBookedRoomExistenceByCustomerId(bookedRoomsByCustomer);
        bookedRoomRepository.deleteAll(bookedRoomsByCustomer);
        return (CustomerDTO) DomainToDTOConverter.convertObjToDTO(customer.get(), CustomerDTO.class);
    }

    public void cancelBookedRoom(final Long bookedRoomId){
        Optional<BookedRoom> bookedRoom = bookedRoomRepository.findById(bookedRoomId);
        validationControlService.verifyBookedRoomExistence(bookedRoom);
        bookedRoomRepository.delete(bookedRoom.get());
    }
}
