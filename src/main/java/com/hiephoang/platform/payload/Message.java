package com.hiephoang.platform.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String username;

    private String message;

    private long timestamp;

    public static Message parseFrom(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Message.class);
    }

    @Override
    public String toString() {
        return String.format("{\"username\": \"%s\", \"message\": \"%s\", \"timestamp\": %s}", username, message, System.currentTimeMillis());
    }
}
