package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.log.LogHandler;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestLogFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String REQUEST_TIMER = "org.smigo.config.request-timer";
    private final WebApplicationContext context;
    private LogHandler logHandler;

    public RequestLogFilter(WebApplicationContext context) {
        this.context = context;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        log.info("Incoming request:" + req.getMethod() + req.getRequestURL().toString());
        request.setAttribute(REQUEST_TIMER, System.nanoTime());
        chain.doFilter(request, response);
        logHandler.log(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        logHandler = context.getBean(LogHandler.class);
    }

    @Override
    public void destroy() {
        //do nothing
    }
}