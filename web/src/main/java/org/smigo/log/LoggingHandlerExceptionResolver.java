package org.smigo.log;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
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
import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingHandlerExceptionResolver implements HandlerExceptionResolver, PriorityOrdered {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MailHandler mailHandler;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        final LogBean logBean = LogBean.create(request, response);
        log.error("Error during request(Inside Spring MVC). Handler:" + handler + logBean, ex);
        if (ex != null) {
            final String note = ex.getClass().getName() + ":" + ex.getMessage();
            mailHandler.sendAdminNotification("error during request inside Spring MVC", note + "\n" + logBean);
            request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, note);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
