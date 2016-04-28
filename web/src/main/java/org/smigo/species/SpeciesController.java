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
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Collection<Species> getSpecies(Locale locale) {
        return speciesHandler.getDefaultSpecies(locale);
    }

    @RequestMapping(value = "/rest/species/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Species getSpecies(@PathVariable int id, @AuthenticationPrincipal AuthenticatedUser user, Locale locale) {
        return speciesHandler.getSpecies(id, locale);
    }

    @RequestMapping(value = "/rest/species", method = RequestMethod.POST)
    @ResponseBody
    public Object addSpecies(@Valid @RequestBody Vernacular name, BindingResult result,
                             @AuthenticationPrincipal AuthenticatedUser user, Locale locale, HttpServletResponse response) {
        log.info("Adding species. Name:" + name.getVernacularName());
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return result.getAllErrors();
        }
        return speciesHandler.addSpecies(name.getVernacularName(), user, locale);
    }

    @RequestMapping(value = "/rest/species/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateSpecies(@Valid @RequestBody Species species, BindingResult result, @PathVariable int id,
                                @AuthenticationPrincipal AuthenticatedUser user, HttpServletResponse response, Locale locale) {
        log.info("Updating species. Species:" + species);
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return result.getAllErrors();
        }
        Review review = speciesHandler.updateSpecies(id, species, user);
        if (review == Review.MODERATOR) {
            response.setStatus(202);
        }
        return speciesHandler.getSpecies(id, locale);
    }

    @RequestMapping(value = "/rest/species/search", method = RequestMethod.POST)
    @ResponseBody
    public List<Species> searchSpecies(@Valid @RequestBody SpeciesSearch species, Locale locale) {
        log.info("Searching species, query:" + species.getQuery());
        return speciesHandler.searchSpecies(species.getQuery(), locale);
    }

    @PreAuthorize("hasAuthority('" + AuthenticatedUser.MOD_AUTHORITY + "')")
    @RequestMapping(value = "/rest/species/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteSpecies(Locale locale, @PathVariable int id) {
        speciesHandler.deleteSpecies(id);
    }

    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    @RequestMapping(value = "/rest/species/search", method = RequestMethod.GET)
    public void searchSpecies() {
    }


    @RequestMapping(value = "/rest/vernacular", method = RequestMethod.GET)
    @ResponseBody
    public List<Vernacular> getVernacular(Locale locale) {
        log.info("Getting vernacular");
        return speciesHandler.getVernacular(locale);
    }

    @RequestMapping(value = "/rest/vernacular", method = RequestMethod.POST)
    @ResponseBody
    public Object addVernacular(@Valid @RequestBody Vernacular vernacular, BindingResult result,
                                @AuthenticationPrincipal AuthenticatedUser user, Locale locale, HttpServletResponse response) {
        log.info("Adding vernacular:" + vernacular);
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return result.getAllErrors();
        }
        CrudResult review = speciesHandler.addVernacular(vernacular, user, locale);
        if (review.getReview() == Review.MODERATOR) {
            response.setStatus(HttpStatus.ACCEPTED.value());
        }
        return review.getId();
    }

    @RequestMapping(value = "/rest/vernacular/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteVernacular(@PathVariable int id, @AuthenticationPrincipal AuthenticatedUser user, Locale locale, HttpServletResponse response) {
        log.info("Deleting vernacular. Name:" + id);
        Review review = speciesHandler.deleteVernacular(id, user, locale);
        if (review == Review.MODERATOR) {
            response.setStatus(HttpStatus.ACCEPTED.value());
        }
        return null;
    }
}
