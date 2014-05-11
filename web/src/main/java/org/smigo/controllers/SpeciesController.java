package org.smigo.controllers;

import jutil.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.entities.User;
import org.smigo.formbean.RuleFormModel;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.smigo.propertyeditors.FamilyPropertyEditor;
import org.smigo.propertyeditors.RuleTypePropertyEditor;
import org.smigo.propertyeditors.SpeciesPropertyEditor;
import org.smigo.species.SpeciesHandler;
import org.sourceforge.kga.Family;
import org.sourceforge.kga.Species;
import org.sourceforge.kga.rules.RuleType;
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

  public SpeciesController() {
    log.debug("Creating new SpeciesController");
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Family.class, new FamilyPropertyEditor());
    binder.registerCustomEditor(Species.class, new SpeciesPropertyEditor());
    binder.registerCustomEditor(RuleType.class, new RuleTypePropertyEditor());
  }

  @ModelAttribute
  public void populateModel(Model model) {
    model.addAttribute("families", Collections.sort(userSession.getFamilies()));
    model.addAttribute("ruleTypes", RuleType.values());
    model.addAttribute("listofallspecies", userSession.getAllSpecies());
  }

  /**
   * Returns a list of all species for overview
   */
  @RequestMapping(value = "/listspecies")
  public ModelAndView getSpecieslist() {
    return new ModelAndView("specieslist.jsp");
  }

  /**
   * Returns details of a species
   */
  @RequestMapping(value = "/species/{id}")
  public ModelAndView getSpecies(@PathVariable Integer id) {
    return new ModelAndView("speciesinfo.jsp", "species", userSession.getSpecies(id));
  }

  /**
   * Delete a species
   */
  @RequestMapping(value = "/deletespecies/{id}")
  public String deleteSpecies(@PathVariable Integer id, Model model) {
    Species speciesToDelete = userSession.getSpecies(id);
    databaseresource.deleteSpecies(userSession.getUser(), id);
    userSession.reloadSpecies();
	model.addAttribute("message", "general.deleted");
	model.addAttribute("argument1", speciesToDelete.getTranslation());
    return "message.jsp";
  }

  /**
   * Returns a form for adding/editing species
   */
  @RequestMapping(value = {"/update-species", "/add-species"}, method = RequestMethod.GET)
  public ModelAndView getSpeciesForm(@RequestParam(value = "id", required = false) Integer id) {
    ModelAndView mav = new ModelAndView("speciesform.jsp");
    Species s = id == null ? new Species() : userSession.getSpecies(id);
    mav.addObject("species", s);
    return mav;
  }

  /**
   * Handles the edit species form.
   *
   * @return
   */
  @RequestMapping(value = "/update-species", method = RequestMethod.POST)
  public String handleSpeciesForm(@Valid Species species, BindingResult result) {
    log.debug("Handle speciesform " + species);
    if (result.hasErrors()) {
      return "speciesform.jsp";
    }
    int id = speciesHandler.updateSpecies(species);
    return "redirect:/species/" + id;

  }

  @RequestMapping("/display")
  public
  @ResponseBody
  String setDisplay(@RequestParam Integer speciesId,
                    @RequestParam(required = false) Boolean display) {
    log.debug("Display " + speciesId + "," + display);
    User user = userSession.getUser();
    databaseresource.setSpeciesVisibility(speciesId, user, display == null ? !userSession.getSpecies(speciesId).isDisplay() : display);
    userSession.reloadSpecies();
    userSession.reloadGarden();
    return "";
  }

  @RequestMapping("/deleterule")
  public
  @ResponseBody
  String setDisplay(@RequestParam Integer ruleId) {
    log.debug("Deleting rule:" + ruleId);
    User user = userSession.getUser();
    if (userSession.getRule(ruleId).getCreatorId() == 0)
      databaseresource.setRulesVisibility(ruleId, user, false);
    else
      databaseresource.deleteRule(ruleId, user);
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
    databaseresource.addRule(rule, userSession.getUser());
    userSession.reloadSpecies();
    userSession.reloadGarden();
    return "redirect:/species/" + rule.getHost();
  }
}