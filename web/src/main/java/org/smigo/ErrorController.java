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
import org.smigo.log.VisitLogger;
import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailHandler mailHandler;

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Model model, HttpServletRequest request) {
        final Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        final String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        final String note = (String) request.getAttribute(VisitLogger.NOTE_ATTRIBUTE);
        log.error("Error during request. (Outside Spring MVC) Statuscode:" + statusCode, exception);
        String exMsg = exception == null ? "" : exception.getClass().getName() + ":" + exception.getMessage();
        String uriMsg = uri == null ? "" : "Uri:" + uri;
        String noteMsg = note == null ? "" : note;
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, noteMsg + "," + exMsg + "," + uriMsg);
        model.addAttribute("statusCode", statusCode);

        if (statusCode != HttpStatus.NOT_FOUND.value()) {
            mailHandler.sendAdminNotification("error during request outside Spring MVC", uri + " - " + exMsg);
        }
        return "error.jsp";
    }
}
