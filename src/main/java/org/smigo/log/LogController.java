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
import org.smigo.plants.PlantHolder;
import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Controller
public class LogController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlantHolder plantHolder;
    @Autowired
    private MailHandler mailHandler;
    @Autowired
    private LogHandler logHandler;

    @RequestMapping(value = "/error")
    public String error(Model model, HttpServletRequest request, HttpServletResponse response) {
        Exception ex = (Exception) request.getAttribute("javax.servlet.error.exception");
        logHandler.logError(request, response, ex, "Outside Spring MVC");
        model.addAttribute("statusCode", request.getAttribute("javax.servlet.error.status_code"));
        return "error.jsp";
    }

    @RequestMapping(value = "/rest/log/error", method = RequestMethod.POST)
    @ResponseBody
    public void logError(@RequestBody ReferenceError referenceError, HttpServletRequest request, HttpServletResponse response) {
        final Log logBean = Log.create(request, response);
        final String msg = "Angular error:\n" + referenceError + "\nPlants:\n" + StringUtils.arrayToDelimitedString(plantHolder.getPlants().toArray(), ",") + logBean;
        log.error(msg);
        mailHandler.sendAdminNotification("angular error", msg);
    }

    @RequestMapping(value = {"/rest/log/feature/*", "/rest/log/feature"}, method = RequestMethod.POST)
    @ResponseBody
    public void logFeatureRequest(@RequestBody FeatureRequest feature, HttpServletRequest request, HttpServletResponse response) {
        mailHandler.sendAdminNotification("feature request", feature.getFeature() + logHandler.getRequestDump(request, response, System.lineSeparator()));
    }

    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    @RequestMapping(value = "/rest/log/error", method = RequestMethod.GET)
    public void logError() {
    }


    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    @RequestMapping(value = "/rest/log/feature", method = RequestMethod.GET)
    public void logFeatureRequest() {
    }

}
