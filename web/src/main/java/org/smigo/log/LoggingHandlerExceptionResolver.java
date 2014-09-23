package org.smigo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        log.error("Error during request(Inside Spring MVC). Handler:" + handler, ex);
        if (ex != null) {
            final String note = ex.getClass().getName() + ":" + ex.getMessage();
            request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, note);
        }
        response.setStatus(500);
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
