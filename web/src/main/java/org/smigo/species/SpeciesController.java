package org.smigo.species;

import kga.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
    public Collection<SpeciesView> getSpecies(@AuthenticationPrincipal AuthenticatedUser user, Locale locale) {
        return speciesHandler.getSpeciesMap(user, locale).values();
    }

    @RequestMapping(value = "/rest/species/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Species getSpecies(@PathVariable int id, @AuthenticationPrincipal AuthenticatedUser user, Locale locale) {
        return speciesHandler.getSpecies(id, locale);
    }

    @RequestMapping(value = "/rest/species", method = RequestMethod.POST)
    @ResponseBody
    public int addSpecies(@Valid @RequestBody SpeciesFormBean species, @AuthenticationPrincipal AuthenticatedUser user,
                          Locale locale, HttpServletResponse response) {
        final Integer speciesId = speciesHandler.addSpecies(species, user, locale);
        if (speciesId == -1) {
            log.warn("User:" + user.getId() + " could not add species:" + species.getVernacularName() + " with locale:" + locale);
            response.setStatus(HttpStatus.CONFLICT.value());
        }
        return speciesId;
    }

    @RequestMapping(value = "/rest/species/search", method = RequestMethod.POST)
    @ResponseBody
    public List<SpeciesView> searchSpecies(@Valid @RequestBody SpeciesSearchBean species, Locale locale) {
        log.info("Searching species, query:" + species.getQuery());
        return speciesHandler.searchSpecies(species.getQuery(), locale);
    }

}