package org.smigo.species;

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
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Controller
public class SpeciesController implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(SpeciesController.class);
    @Autowired
    private SpeciesHandler speciesHandler;

    @RequestMapping(value = "/rest/species", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Species> getSpecies() {
        return speciesHandler.getSpeciesMap();
    }

    @RequestMapping(value = "/rest/species/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Species getSpecies(@PathVariable int id, @AuthenticationPrincipal AuthenticatedUser user, Locale locale) {
        return speciesHandler.getSpecies(id);
    }

    @RequestMapping(value = "/rest/species", method = RequestMethod.POST)
    @ResponseBody
    public Object addSpecies(@Valid @RequestBody SpeciesAdd species, BindingResult result,
                             @AuthenticationPrincipal AuthenticatedUser user, Locale locale, HttpServletResponse response) {
        log.info("Adding species. Name:" + species.getVernacularName());
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return result.getAllErrors();
        }
        return speciesHandler.addSpecies(species.getVernacularName(), user, locale);
    }

    @RequestMapping(value = "/rest/species/search", method = RequestMethod.POST)
    @ResponseBody
    public List<Species> searchSpecies(@Valid @RequestBody SpeciesSearch species, Locale locale) {
        log.info("Searching species, query:" + species.getQuery());
        return speciesHandler.searchSpecies(species.getQuery(), locale);
    }

    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    @RequestMapping(value = "/rest/species/search", method = RequestMethod.GET)
    public void searchSpecies() {
    }

}
