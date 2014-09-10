package org.smigo.user;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.log.VisitLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        List<ObjectError> errors = new ArrayList<ObjectError>();
        if (exception instanceof BadCredentialsException) {
            errors.add(new ObjectError("username", "msg.badcredentials"));
        } else {
            errors.add(new ObjectError("username", "msg.unknownerror"));
        }
        String responseBody = objectMapper.writeValueAsString(errors);
        response.getWriter().append(responseBody);

        final String note = "Authentication Failure:" + request.getParameter("username") + exception.getMessage();
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, note);
        log.info("Authentication Failure " + exception);
    }
}
