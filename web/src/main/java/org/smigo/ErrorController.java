package org.smigo;

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
import org.smigo.log.LogHandler;
import org.smigo.log.VisitLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogHandler logHandler;

    @RequestMapping(value = "/error")
    public String error(Model model, HttpServletRequest request, HttpServletResponse response) {
        final Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        final String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        final String note = (String) request.getAttribute(VisitLogger.NOTE_ATTRIBUTE);
        logHandler.logError(request, response, exception, statusCode, uri);

        model.addAttribute("statusCode", statusCode);
        return "error.jsp";
    }
}
