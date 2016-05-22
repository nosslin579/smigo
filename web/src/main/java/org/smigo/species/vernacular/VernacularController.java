package org.smigo.species.vernacular;

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
import org.smigo.species.CrudResult;
import org.smigo.species.Review;
import org.smigo.species.SpeciesHandler;
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
import java.util.List;
import java.util.Locale;

@Controller
public class VernacularController implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(VernacularController.class);
    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private VernacularHandler vernacularHandler;

    @RequestMapping(value = "/rest/vernacular", method = RequestMethod.GET)
    @ResponseBody
    public List<Vernacular> getAllVernacular(Locale locale) {
        log.info("Getting vernacular");
        return vernacularHandler.getVernacularsByLocale(locale);
    }

    @RequestMapping(value = "/rest/vernacular", method = RequestMethod.GET, params = {"speciesid"})
    @ResponseBody
    public List<Vernacular> getVernacularBySpecies(@RequestParam("speciesid") int speciesId, Locale locale) {
        log.info("Getting vernacular");
        return vernacularHandler.getAnyVernaculars(speciesId, locale);
    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rest/vernacular", method = RequestMethod.POST)
    @ResponseBody
    public Object addVernacular(@Valid @RequestBody Vernacular vernacular, BindingResult result,
                                @AuthenticationPrincipal AuthenticatedUser user, Locale locale, HttpServletResponse response) {
        log.info("Adding vernacular:" + vernacular);
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return result.getAllErrors();
        }
        CrudResult review = vernacularHandler.addVernacular(vernacular, user, locale);
        if (review.getReview() == Review.MODERATOR) {
            response.setStatus(HttpStatus.ACCEPTED.value());
        }
        return review.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rest/vernacular/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateVernacular(@Valid @RequestBody Vernacular vernacular, BindingResult result, @PathVariable int id,
                                   @AuthenticationPrincipal AuthenticatedUser user, Locale locale, HttpServletResponse response) {
        log.info("Updating vernacular:" + vernacular);
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return result.getAllErrors();
        }
        Review review = vernacularHandler.updateVernacular(id, vernacular, user, locale);
        if (review == Review.MODERATOR) {
            response.setStatus(HttpStatus.ACCEPTED.value());
        }
        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rest/vernacular/{id:\\d+}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteVernacular(@PathVariable int id, @AuthenticationPrincipal AuthenticatedUser user, Locale locale, HttpServletResponse response) {
        log.info("Deleting vernacular. Name:" + id);
        Review review = vernacularHandler.deleteVernacular(id, user, locale);
        if (review == Review.MODERATOR) {
            response.setStatus(HttpStatus.ACCEPTED.value());
        }
        return null;
    }
}
