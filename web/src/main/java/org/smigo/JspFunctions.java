package org.smigo;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JspFunctions {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String speciesMessageKey(int id) {
        return "species" + id;
    }

    public static String toJson(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}
