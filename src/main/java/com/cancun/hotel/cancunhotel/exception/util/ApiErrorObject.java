package com.cancun.hotel.cancunhotel.exception.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorObject {

    private HttpStatus status;
    private Integer statusId;
    private String message;
    private String error;
}
