package org.smigo.controllers;

import jutil.Collections;
import kga.Family;
import kga.rules.RuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.CurrentUser;
import org.smigo.SpeciesView;
import org.smigo.entities.User;
import org.smigo.formbean.RuleFormModel;
import org.smigo.formbean.SpeciesFormBean;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.smigo.propertyeditors.FamilyPropertyEditor;
import org.smigo.propertyeditors.RuleTypePropertyEditor;
import org.smigo.propertyeditors.SpeciesPropertyEditor;
import org.smigo.species.SpeciesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.Serializable;

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
    private UserSession userSession;
    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private CurrentUser currentUser;

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
        model.addAttribute("families", Collections.sort(userSession.getFamilies()));
        model.addAttribute("ruleTypes", RuleType.values());
        model.addAttribute("listofallspecies", userSession.getAllSpecies());
    }

    @RequestMapping(value = "/listspecies")
    public ModelAndView getSpecieslist() {
        return new ModelAndView("specieslist.jsp");
    }

    @RequestMapping(value = "/species/{id}")
    public ModelAndView getSpecies(@PathVariable Integer id) {
        return new ModelAndView("speciesinfo.jsp", "species", userSession.getSpecies(id));
    }

    @RequestMapping(value = "/deletespecies/{id}")
    public String deleteSpecies(@PathVariable Integer id, Model model) {
        databaseresource.deleteSpecies(currentUser.getId(), id);
        userSession.reloadSpecies();
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
    public
    @ResponseBody
    String setDisplay(@RequestParam Integer speciesId,
                      @RequestParam(required = false) Boolean display) {
        log.debug("Display " + speciesId + "," + display);
        databaseresource.setSpeciesVisibility(speciesId, currentUser.getId(), display == null ? !userSession.getSpecies(speciesId).isDisplay() : display);
        userSession.reloadSpecies();
        userSession.reloadGarden();
        return "";
    }

    @RequestMapping("/deleterule")
    public
    @ResponseBody
    String setDisplay(@RequestParam Integer ruleId) {
        log.debug("Deleting rule:" + ruleId);
        if (userSession.getRule(ruleId).getCreatorId() == 0)
            databaseresource.setRulesVisibility(ruleId, currentUser.getId(), false);
        else
            databaseresource.deleteRule(ruleId, currentUser.getId());
        userSession.reloadSpecies();
        userSession.reloadGarden();
        return "";
    }

    @RequestMapping(value = {"/addrule"}, method = RequestMethod.GET)
    public String getRuleForm(@RequestParam Integer species, ModelMap model) {
        model.addAttribute("ruleFormModel", new RuleFormModel());
        model.addAttribute("host", userSession.getSpecies(species));
        return "ruleform.jsp";
    }

    @RequestMapping(value = "/addrule", method = RequestMethod.POST)
    public String handleRuleForm(@Valid RuleFormModel rule, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("host", userSession.getSpecies(rule.getHost()));
            return "ruleform.jsp";
        }
        databaseresource.addRule(rule, currentUser.getId());
        userSession.reloadSpecies();
        userSession.reloadGarden();
        return "redirect:/species/" + rule.getHost();
    }
}