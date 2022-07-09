package com.cancun.hotel.cancunhotel.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnavailablePeriodsVO {

    private Map<String,  Map<String, LocalDate>> unavailableDates;
}
