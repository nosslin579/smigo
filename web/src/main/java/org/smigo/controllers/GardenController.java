package org.smigo.controllers;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.entities.User;
import org.smigo.formbean.AddYearFormBean;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.smigo.service.PlantConverter;
import kga.Garden;
import kga.errors.GardenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import java.util.SortedSet;

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
  private DatabaseResource databaseResource;
  @Autowired
  private UserSession userSession;
  @Autowired
  private MessageSource messageSource;

  public GardenController() {
    log.debug("Creating new GardenController");
  }

  @RequestMapping(value = {"/garden", "/"}, method = RequestMethod.GET)
  public String getGarden() {
    SortedSet<Integer> years = userSession.getGarden().getYears();
    int current = Calendar.getInstance().get(Calendar.YEAR);
    return "forward:/garden/" + (years.isEmpty() ? current : years.last());
  }

  /**
   * Get garden at year.
   */
  @RequestMapping(value = "/garden/{year}")
  public ModelAndView getGarden(@PathVariable Integer year) {
    try {
      Garden g = userSession.getGarden();
      log.debug("Displaying garden: " + year + ", squares:" + g.getAllSquares().size() + ", bounds:" + g.getBoundsFor(year));
      ModelAndView mav = new ModelAndView("garden.jsp");
      mav.addObject("year", year);
      mav.addObject("kgagarden", g);
      mav.addObject("specieslist", userSession.getVisibleSpecies());
      mav.addObject("speciesJson", userSession.getVisibleSpecies());
      return mav;
    } catch (Exception e) {
      ModelAndView mav = new ModelAndView("message");
      mav.addObject("message", "Unexpected error - " + e.getMessage());
      log.error("Couldn't get garden ", e);
      return mav;
    }
  }

  /**
   * An ajax call to this method will store the garden in the database. If
   * user is not logged in it stores the garden to session.
   */
  @RequestMapping(value = "/savegarden", produces = "text/plain;charset=UTF-8")
  public
  @ResponseBody
  String saveGarden(@RequestParam(required = false) String jsonstringyearandplants, Locale locale)
    throws JsonParseException, JsonMappingException, IOException {
    log.debug("Savegarden " + jsonstringyearandplants);
    userSession.updateGarden(PlantConverter.convert(jsonstringyearandplants));
	  String loginFirst = messageSource.getMessage("account.pleaseloginfirst", null, locale);
	  String ok = messageSource.getMessage("ok", null, locale);
	  return userSession.getUser().getId() == 0 ? loginFirst : ok;
  }

  /**
   * Returns the add year form.
   */
  @RequestMapping(value = "/addyear", method = RequestMethod.GET)
  public ModelAndView getAddYearForm() {
    ModelAndView mav = new ModelAndView("addyearform.jsp", "suggestedyear", 2014);
    mav.addObject("addYearFormBean", new AddYearFormBean());
    return mav;
  }

  /**
   * Handles the add year form. Adds a new year to garden. Copies perennials
   * and items from last year.
   */
  @RequestMapping(value = "/addyear", method = RequestMethod.POST)
  public String handleAddYearForm(ModelMap model, @Valid AddYearFormBean addYearFormBean, BindingResult result) {
    log.debug("Adding year " + addYearFormBean.getYear());
    if (result.hasErrors()) {
      log.warn("Can not add year");
      return "addyearform.jsp";
    }
    try {
      int newYear = addYearFormBean.getYear();
      Garden g = userSession.getGarden();
      g.addYear(newYear);
      userSession.updateGarden(PlantConverter.convert(g.getSquaresFor(newYear)));
      return "redirect:garden/" + newYear;
    } catch (GardenException e) {
      result.rejectValue("year", e.getMessageKey());
      return "addyearform.jsp";
    } catch (Exception e) {
      log.error("error adding year ", e);
      model.addAttribute("message", "error.addyear");
      return "message.jsp";
    }
  }

  /**
   * Returns the delete year form
   *
   * @return
   */
  @RequestMapping(value = "/deleteyear", method = RequestMethod.GET)
  public ModelAndView getDeleteYearForm() {
    return new ModelAndView("deleteyearform.jsp");
  }

  /**
   * Handles the delete year form. Deletes an entire year.
   */
  @RequestMapping(value = "/deleteyear", method = RequestMethod.POST)
  public String handleDeleteYearForm(ModelMap model, @RequestParam("deleteyear") Integer deleteyear) {
    log.debug("Delete year " + deleteyear);
    try {
      User user = userSession.getUser();
      databaseResource.deleteYear(user, deleteyear);
      userSession.reloadGarden();
      return "redirect:garden";
    } catch (Exception e) {
      log.error("error deleting year ", e);
      model.addAttribute("message", "error.deleteyear");
      return "message.jsp";
    }
  }

  @RequestMapping(value = "/referrer", method = RequestMethod.POST)
  @ResponseBody
  public void saveReferrer(@RequestBody String referrer) {
    log.info("document.referrer: " + referrer);
  }

}