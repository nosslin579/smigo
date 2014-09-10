package org.smigo.controllers;

import kga.Garden;
import kga.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.garden.UpdateGardenBean;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.UserAdaptiveMessageSource;
import org.smigo.user.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Locale;

/**
 * Controller that handles Garden specific type of requests.
 *
 * @author Christian Nilsson
 */
@Controller
@RequestMapping("/")
public class GardenController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserHandler userHandler;
    @Autowired
    private UserAdaptiveMessageSource messageSource;
    @Autowired
    private SpeciesHandler speciesHandler;

    public GardenController() {
        log.debug("Creating new GardenController");
    }

    @RequestMapping(value = {"/garden", "/"}, method = RequestMethod.GET)
    public String getGarden(Model model, Locale locale, Principal principal) {
        model.addAttribute("user", userHandler.getUser(principal));
        model.addAttribute("garden", speciesHandler.getGarden());
        model.addAttribute("messages", messageSource.getAllMessages(locale));
        return "ng.jsp";
    }

    @RequestMapping(value = "/rest/garden", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Garden getGarden() {
        return speciesHandler.getGarden();
    }

    @RequestMapping(value = {"/update-garden", "/rest/garden"}, produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public void updateGarden(@RequestBody UpdateGardenBean updateGardenBean) {
        speciesHandler.updateGarden(updateGardenBean);
    }


    @RequestMapping(value = "/garden/{year}")
    public String getGarden(@PathVariable Integer year, Model model) {
        Garden g = speciesHandler.getGarden();
//        log.debug("Displaying garden: " + year + ", squares:" + g.getSquares().size() + ", bounds:" + g.getBoundsFor(year));
        model.addAttribute("year", year);
        model.addAttribute("kgagarden", g);
        final Collection<? extends Species> visibleSpecies = speciesHandler.getGarden().getSpecies().values();
        model.addAttribute("specieslist", visibleSpecies);
        model.addAttribute("speciesJson", visibleSpecies);
        return "garden.jsp";
    }

    @RequestMapping(value = "/referrer", method = RequestMethod.POST)
    @ResponseBody
    public void saveReferrer(@RequestBody String referrer) {
        log.info("document.referrer: " + referrer);
    }

}