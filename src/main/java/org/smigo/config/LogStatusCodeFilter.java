package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogStatusCodeFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void destroy() {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse r= (HttpServletResponse) response;
        log.info("Response code:"+r.getStatus() + " for " + request.getRemoteAddr());
        chain.doFilter(request, response);
        log.info("Response code:" + r.getStatus() + " for " + ((HttpServletRequest) request).getServletPath());
        if (r.getStatus()==304)
        {
        log.info("OK");
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        //do nothing
    }
}