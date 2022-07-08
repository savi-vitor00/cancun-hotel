package com.cancun.hotel.cancunhotel.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostumerDTO {

    private Long id;

    private String name;
}
