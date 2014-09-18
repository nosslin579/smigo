package org.smigo.species;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;

@Controller
public class SpeciesController implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(SpeciesController.class);
    @Autowired
    private SpeciesHandler speciesHandler;

    @RequestMapping(value = "/rest/species", method = RequestMethod.GET)
    @ResponseBody
    public Collection<SpeciesView> getSpecies(@AuthenticationPrincipal AuthenticatedUser user) {
        return speciesHandler.getSpeciesMap().values();
    }

    @RequestMapping(value = "/rest/species", method = RequestMethod.POST)
    @ResponseBody
    public int addSpecies(@Valid SpeciesFormBean species, @AuthenticationPrincipal AuthenticatedUser user) {
        return speciesHandler.addSpecies(species, user);
    }

}