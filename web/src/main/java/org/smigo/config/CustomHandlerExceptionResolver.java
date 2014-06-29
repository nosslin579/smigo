package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.listener.VisitLogger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver{
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("Error during request", ex);
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, "Exception:" + ex.getMessage());
        return new ModelAndView("error.jsp");
    }
}
