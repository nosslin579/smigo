package org.smigo.species;

import kga.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public Species getSpecies(@PathVariable int id, @AuthenticationPrincipal AuthenticatedUser user) {
        return speciesHandler.getSpecies(id);
    }

    @RequestMapping(value = "/rest/species", method = RequestMethod.POST)
    @ResponseBody
    public int addSpecies(@Valid @RequestBody SpeciesFormBean species, @AuthenticationPrincipal AuthenticatedUser user) {
        return speciesHandler.addSpecies(species, user);
    }

    @RequestMapping(value = "/rest/species/search", method = RequestMethod.POST)
    @ResponseBody
    public List<SpeciesView> searchSpecies(@Valid @RequestBody SpeciesSearchBean species, Locale locale) {
        return speciesHandler.searchSpecies(species.getQuery(), locale);
    }

}