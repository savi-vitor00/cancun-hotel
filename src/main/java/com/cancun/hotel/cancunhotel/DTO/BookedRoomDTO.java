package com.cancun.hotel.cancunhotel.DTO;

import com.cancun.hotel.cancunhotel.domain.Costumer;
import com.cancun.hotel.cancunhotel.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoomDTO {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Room room;

    private Costumer costumer;
}
