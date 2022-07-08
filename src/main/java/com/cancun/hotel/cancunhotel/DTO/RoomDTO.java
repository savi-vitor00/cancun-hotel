package com.cancun.hotel.cancunhotel.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO{

        private Long id;

        private Integer roomNumber;

        private String description;
}
