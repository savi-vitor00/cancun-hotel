package com.cancun.hotel.cancunhotel.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DomainToDTOConverter {

    public static Object convertObjToDTO(Object o, TypeReference clazz) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(o, clazz);
    }
}
