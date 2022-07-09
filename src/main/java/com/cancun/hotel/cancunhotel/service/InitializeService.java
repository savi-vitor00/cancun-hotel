package com.cancun.hotel.cancunhotel.service;

import com.cancun.hotel.cancunhotel.DTO.CustomerDTO;
import com.cancun.hotel.cancunhotel.DTO.RoomDTO;
import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.cancun.hotel.cancunhotel.repository.CustomerRepository;
import com.cancun.hotel.cancunhotel.repository.RoomRepository;
import com.cancun.hotel.cancunhotel.util.DomainToDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InitializeService {

    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public InitializeService(CustomerRepository customerRepository, RoomRepository roomRepository){
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    public Map<String, List<Object>> insertObjectsForTestingAPIPurpose() {
        Map<String, List<Object>> hotelInitializeMap = new HashMap<String, List<Object>>();
        RoomDTO room = initializeRoom();
        List<CustomerDTO> customerList = createCustomers();
        hotelInitializeMap.put("Created Room", Arrays.asList(room));
        hotelInitializeMap.put("Created Customers", Arrays.asList(customerList));
        return hotelInitializeMap;
    }

    private List<CustomerDTO> createCustomers() {
        List<CustomerDTO> customerList = new ArrayList<>();
        customerList.add(initializeCustomer("Jim Halpert"));
        customerList.add(initializeCustomer("John Winchester"));
        customerList.add(initializeCustomer("Dustin Henderson"));
        return customerList;
    }

    public RoomDTO initializeRoom(){
        Room room = new Room();
        room.setRoomNumber(1);
        room.setDescription("Master Bedroom");
        room = roomRepository.save(room);
        return (RoomDTO) DomainToDTOConverter.convertObjToDTO(room, RoomDTO.class);
    }

    public CustomerDTO initializeCustomer(final String name){
        Customer customer = new Customer();
        customer.setName(name);
        customer = customerRepository.save(customer);
        return (CustomerDTO) DomainToDTOConverter.convertObjToDTO(customer, CustomerDTO.class);
    }

    public Boolean verifyInitialization(){
        List<Room> rooms = roomRepository.findAll();
        if(rooms != null && rooms.size() > 0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
