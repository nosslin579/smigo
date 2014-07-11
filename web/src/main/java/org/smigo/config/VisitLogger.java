package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.persitance.DatabaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisitLogger extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String NOTE_ATTRIBUTE = "LogVisitNote";

    @Autowired
    private DatabaseResource dbr;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String requestedURL = request.getRequestURL().toString();
        if (requestedURL.endsWith(".js") || requestedURL.endsWith(".png") || requestedURL.endsWith(".css")) {
            log.debug("Not logging " + requestedURL);
        } else if (request.getDispatcherType() != DispatcherType.FORWARD) {
            dbr.logVisit(request);
        }
    }
}
