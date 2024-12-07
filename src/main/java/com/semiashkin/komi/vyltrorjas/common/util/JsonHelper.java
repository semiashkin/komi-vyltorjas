package com.semiashkin.komi.vyltrorjas.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    public static String writeObjectAsString(Object object) {
        return MAPPER.writeValueAsString(object);
    }
}
