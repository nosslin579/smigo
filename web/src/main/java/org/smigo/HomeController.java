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
import org.smigo.plants.PlantHandler;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserAdaptiveMessageSource;
import org.smigo.user.UserHandler;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

@Controller
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserSession userSession;
    @Autowired
    private UserHandler userHandler;
    @Autowired
    private UserAdaptiveMessageSource messageSource;
    @Autowired
    private PlantHandler plantHandler;
    @Autowired
    private SpeciesHandler speciesHandler;

    @RequestMapping(value = {
            "/", "/garden", "/hasta-luego", "/help", "/login", "/register", "/wall/*", "/beta", "/account", "/species/*",
            "/rule/*", "/forum", "/request-password-link"
    }, method = RequestMethod.GET)
    public String getGarden(Model model, Locale locale, @AuthenticationPrincipal AuthenticatedUser user, HttpServletRequest request) {
        if (user != null && !userSession.getUser().isTermsOfService()) {
            return "redirect:/accept-termsofservice";
        }
        final Map<Object, Object> allMessages = messageSource.getAllMessages(locale);
        allMessages.putAll(speciesHandler.getSpeciesTranslation(locale));

        model.addAttribute("user", userHandler.getUser(user));
        model.addAttribute("species", speciesHandler.getSpeciesMap());
        model.addAttribute("plantData", plantHandler.getPlants(user));
        model.addAttribute("messages", allMessages);
        model.addAttribute("rules", speciesHandler.getRules());
        model.addAttribute("addEscapeFragment", request.getServletPath().matches("/help|/forum|/login|/register|/"));
        model.addAttribute("addRobotsNoIndex", request.getServletPath().matches("/account|/wall.+|/species.+|/rule.+|/request-password-link"));
        return "ng.jsp";
    }

    @RequestMapping(value = {"/", "/help", "/login", "/register", "/forum"}, method = RequestMethod.GET, params = "_escaped_fragment_")
    public String getGarden(HttpServletRequest request) {
        final String jsp = request.getServletPath().equals("/") ? "/home" : request.getServletPath();
        return "escaped_fragment" + jsp + ".jsp";
    }


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
        return "error.jsp";

    }
}
