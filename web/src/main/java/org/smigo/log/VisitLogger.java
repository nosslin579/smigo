package org.smigo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class VisitLogger extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String REQUEST_TIMER = "request-timer";
    public static final String NOTE_ATTRIBUTE = "LogVisitNote";

    @Autowired
    private LogHandler logHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(REQUEST_TIMER, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        final long start = (Long) request.getAttribute(REQUEST_TIMER);
        log.info("Request finished in " + (System.nanoTime() - start) + "ns which is " + (System.nanoTime() - start) / 1000000 + "ms");
        logHandler.log(request,response);
    }

}
