package com.backend.filter;

import com.backend.dto.bases.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class LoginErrorSender {

    private final ObjectMapper objectMapper;

    public LoginErrorSender(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void send(HttpServletResponse response, String message, int status) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(message);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertObjectToJson(errorResponse));
    }

    private String convertObjectToJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }
}
