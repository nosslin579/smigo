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
import org.smigo.plants.PlantHandler;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserAdaptiveMessageSource;
import org.smigo.user.UserHandler;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @ModelAttribute
    public void addDefaultModel(Model model, Locale locale, @AuthenticationPrincipal AuthenticatedUser user) {
        final Map<Object, Object> allMessages = messageSource.getAllMessages(locale);
        allMessages.putAll(speciesHandler.getSpeciesTranslation(locale));
        model.addAttribute("user", userHandler.getUser(user));
        model.addAttribute("species", speciesHandler.getSpeciesMap());
        model.addAttribute("plantData", plantHandler.getPlants(user));
        model.addAttribute("messages", allMessages);
        model.addAttribute("rules", speciesHandler.getRules());
    }

    @RequestMapping(value = {"/garden-planner", "/login", "/register", "/forum", "/account", "/request-password-link"}, method = RequestMethod.GET)
    public String getGarden(Model model, HttpServletRequest request) {
        final String path = request.getServletPath().replace("/", "");
        model.addAttribute("msgTitle", "msg.concat.title." + path);
        model.addAttribute("msgDescription", "msg.concat.metadescription." + path);
        return "ng.jsp";
    }

    @RequestMapping(value = {"/wall/{userName}"}, method = RequestMethod.GET)
    public String getWall(@PathVariable String userName, Model model) {
        model.addAttribute("msgTitle", "msg.title.wall");
        model.addAttribute("titleArg", userName);
        return "ng.jsp";
    }

    @RequestMapping(value = {"/species/{id}"}, method = RequestMethod.GET)
    public String getSpecies(@PathVariable int id, Model model, Locale locale) {
        model.addAttribute("msgTitle", "msg.title.species");
        model.addAttribute("msgDescription", "msg.metadescription.species");
        Object translation = ((Map) model.asMap().get("messages")).get("msg.species" + id);
        model.addAttribute("titleArg", translation);
        model.addAttribute("descriptionArg", translation);
        return "ng.jsp";
    }

    @RequestMapping(value = {"/rule/{id}"}, method = RequestMethod.GET)
    public String getRule(@PathVariable int id, Model model, Locale locale) {
        model.addAttribute("msgTitle", "msg.title.rule");
        model.addAttribute("titleArg", id);
        return "ng.jsp";
    }
}
