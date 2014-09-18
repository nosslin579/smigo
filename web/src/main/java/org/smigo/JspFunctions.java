package org.smigo;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JspFunctions {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String speciesMessageKey(int id) {
        return "species" + id;
    }

    public static String toJson(Object o) throws IOException {
        return o == null ? "null" : objectMapper.writeValueAsString(o);
    }
}
