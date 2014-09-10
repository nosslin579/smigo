package org.smigo.species;

import kga.Family;
import kga.rules.RuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller that handles species specific type of requests.
 *
 * @author Christian Nilsson
 */
@Controller
public class SpeciesController implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(SpeciesController.class);
    @Autowired
    private SpeciesHandler speciesHandler;

    public SpeciesController() {
        log.debug("Creating new SpeciesController");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Family.class, new FamilyPropertyEditor());
        binder.registerCustomEditor(SpeciesView.class, new SpeciesPropertyEditor());
        binder.registerCustomEditor(RuleType.class, new RuleTypePropertyEditor());
    }

    @ModelAttribute
    public void populateModel(Model model) {
        List<Family> ret = new ArrayList<Family>(speciesHandler.getFamilies());
        java.util.Collections.sort(ret);
        model.addAttribute("families", ret);
        model.addAttribute("ruleTypes", RuleType.values());
        model.addAttribute("listofallspecies", speciesHandler.getGarden().getSpecies());
    }

    @RequestMapping(value = "/species", produces = "application/json")
    @ResponseBody
    public List<SpeciesView> getSpecies() {
        final List<SpeciesView> ret = new ArrayList<SpeciesView>();
        ret.add(new SpeciesView(1, "Frangus Saladus", false, true, new Family(1, "Frngium")));
        ret.add(new SpeciesView(2, "Brassica Capitata", false, true, new Family(2, "Brazzicum")));
        return ret;
    }

}