package com.cancun.hotel.cancunhotel.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DomainToDTOConverter {

    public static Object convertObjToDTO(Object o, Class<?> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.convertValue(o, clazz);
    }
}
