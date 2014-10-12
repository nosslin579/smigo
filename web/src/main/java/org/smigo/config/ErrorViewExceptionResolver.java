package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ErrorViewExceptionResolver implements HandlerExceptionResolver, Ordered {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("Invalidating session and removing persistent token:" + request.getUserPrincipal());
        try {
            request.logout();
        } catch (ServletException e) {
            log.error("Could not logout user " + request.getUserPrincipal(), e);
        }
        response.setStatus(500);
        return new ModelAndView("error.jsp", "statusCode", 500);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
