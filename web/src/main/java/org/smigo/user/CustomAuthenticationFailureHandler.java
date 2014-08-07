package org.smigo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.config.VisitLogger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        final String note = "Authentication Failure:" + request.getParameter("username") + exception.getMessage();
        final String noteLengthChecked = note.length() > 255 ? note.substring(0, 255) : note;
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, noteLengthChecked);
        log.info("Authentication Failure " + exception);
    }
}
