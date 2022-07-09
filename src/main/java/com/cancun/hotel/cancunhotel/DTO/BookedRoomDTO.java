package com.cancun.hotel.cancunhotel.DTO;

import com.cancun.hotel.cancunhotel.domain.Customer;
import com.cancun.hotel.cancunhotel.domain.Room;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoomDTO {

    private Long id;

    @JsonProperty("startDate")
    private LocalDate startDate;

    @JsonProperty("endDate")
    private LocalDate endDate;

    private Room room;

    private Customer customer;
}
