package org.smigo.config;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 - 2016 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.log.LogHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestLogFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String REQUEST_TIMER = "org.smigo.config.request-timer";
    private LogHandler logHandler;

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
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        logHandler = ctx.getBean(LogHandler.class);
    }

    @Override
    public void destroy() {
        //do nothing
    }
}