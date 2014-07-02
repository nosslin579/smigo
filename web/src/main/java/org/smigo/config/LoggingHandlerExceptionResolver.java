package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.listener.VisitLogger;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingHandlerExceptionResolver implements HandlerExceptionResolver, PriorityOrdered {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("Error during request. Handler:" + handler, ex);
        final String note = "Exception:" + ex.getMessage();
        final String noteLengthChecked = note.length() > 255 ? note.substring(0, 255) : note;
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, noteLengthChecked);
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
