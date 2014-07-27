package org.smigo.species;

import kga.Family;
import kga.rules.RuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.persitance.DatabaseResource;
import org.smigo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
    private DatabaseResource databaseresource;
    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private User user;

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
        List<Family> ret = new ArrayList<Family>(speciesHandler.getFamilies().values());
        java.util.Collections.sort(ret);
        model.addAttribute("families", ret);
        model.addAttribute("ruleTypes", RuleType.values());
        model.addAttribute("listofallspecies", speciesHandler.getAllSpecies());
    }

    @RequestMapping(value = "/listspecies")
    public ModelAndView getSpecieslist() {
        return new ModelAndView("specieslist.jsp");
    }

    @RequestMapping(value = "/species", produces = "application/json")
    @ResponseBody
    public List<SpeciesView> getSpecies() {
        final List<SpeciesView> ret = new ArrayList<SpeciesView>();
        ret.add(new SpeciesView(1, "Frangus Saladus", false, true, new Family("Frngium", 1)));
        ret.add(new SpeciesView(2, "Brassica Capitata", false, true, new Family("Brazzicum", 2)));
        return ret;
    }

    @RequestMapping(value = "/species1/{id}")
    public ModelAndView getSpecies(@PathVariable Integer id) {
        return new ModelAndView("speciesinfo.jsp", "species", speciesHandler.getSpecies(id));
    }

    @RequestMapping(value = "/deletespecies/{id}")
    public String deleteSpecies(@PathVariable Integer id, Model model) {
        databaseresource.deleteSpecies(user.getId(), id);
        model.addAttribute("speciesId", id);
        return "species-deleted.jsp";
    }

    @RequestMapping(value = {"/update-species", "/add-species"}, method = RequestMethod.GET)
    public String getSpeciesForm() {
        return "speciesform.jsp";
    }

    @RequestMapping(value = "/add-species", method = RequestMethod.POST)
    public String handleSpeciesForm(@Valid SpeciesFormBean speciesFormBean, BindingResult result) {
        if (result.hasErrors()) {
            return "speciesform.jsp";
        }
        int id = speciesHandler.addSpecies(speciesFormBean);
        return "redirect:/species/" + id;
    }

    @RequestMapping("/display")
    @ResponseBody
    public String setDisplay(@RequestParam Integer speciesId, @RequestParam(required = false) Boolean display) {
        log.debug("Display " + speciesId + "," + display);
        databaseresource.setSpeciesVisibility(speciesId, user.getId(), display == null ? !speciesHandler.getSpecies(speciesId).isDisplay() : display);
        return "";
    }

    @RequestMapping("/deleterule")
    @ResponseBody
    public String setDisplay(@RequestParam Integer ruleId) {
        log.debug("Deleting rule:" + ruleId);
        if (speciesHandler.getRule(ruleId).getCreatorId() == 0)
            databaseresource.setRulesVisibility(ruleId, user.getId(), false);
        else
            databaseresource.deleteRule(ruleId, user.getId());
        return "";
    }

    @RequestMapping(value = {"/addrule"}, method = RequestMethod.GET)
    public String getRuleForm(@RequestParam Integer species, ModelMap model) {
        model.addAttribute("ruleFormModel", new RuleFormModel());
        model.addAttribute("host", speciesHandler.getSpecies(species));
        return "ruleform.jsp";
    }

    @RequestMapping(value = "/addrule", method = RequestMethod.POST)
    public String handleRuleForm(@Valid RuleFormModel rule, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("host", speciesHandler.getSpecies(rule.getHost()));
            return "ruleform.jsp";
        }
        databaseresource.addRule(rule, user.getId());
        return "redirect:/species/" + rule.getHost();
    }
}