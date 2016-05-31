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
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SpeciesHandler speciesHandler;

    @ModelAttribute
    public void addDefaultModel(Model model, Locale locale, @AuthenticationPrincipal AuthenticatedUser user) {
        model.addAttribute("species", speciesHandler.getDefaultSpecies());
        model.addAttribute("rules", speciesHandler.getRules());
    }

    @RequestMapping(value = {"/garden-planner", "/login", "/register", "/forum", "/account", "/request-password-link", "/accept-terms-of-service", "/welcome-back"}, method = RequestMethod.GET)
    public String getGarden(Model model, HttpServletRequest request, HttpServletResponse response) {
        final String path = request.getServletPath().replace("/", "");
        model.addAttribute("msgTitle", "msg.concat.title." + path);
        model.addAttribute("msgDescription", "msg.concat.metadescription." + path);
        return "ng.jsp";
    }

    @RequestMapping(value = {"/gardener/{userName}"}, method = RequestMethod.GET)
    public String getWall(@PathVariable String userName, Model model) {
        model.addAttribute("msgTitle", "msg.title.wall");
        model.addAttribute("titleArg", userName);
        return "ng.jsp";
    }
}
