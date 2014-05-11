package org.smigo.controllers;

import org.smigo.entities.Message;
import org.smigo.persitance.DatabaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

@Controller
public class AboutController {

  private DatabaseResource databaseResource;

  @Autowired
  public AboutController(DatabaseResource databaseResource) {
    this.databaseResource = databaseResource;
  }

  @RequestMapping(value = "/about", method = RequestMethod.GET)
  public ModelAndView getAbout() {
    return new ModelAndView("about.jsp");
  }

  @RequestMapping(value = "/help", method = RequestMethod.GET)
  public ModelAndView getHelp() throws SQLException {
    return new ModelAndView("help.jsp");
  }

  @RequestMapping(value = "/hastalavista", method = RequestMethod.GET)
  public ModelAndView hastalavista() {
    return new ModelAndView("message.jsp", "translatedmessage", "hastalavista");
  }

  @RequestMapping(value = "/error", method = RequestMethod.GET)
  public ModelAndView error() {
    return new ModelAndView("message.jsp", "translatedmessage", "pagenotavailable");
  }

  @RequestMapping(value = "/posthelp", method = RequestMethod.POST)
  public ModelAndView handleHelpForm(@Valid Message message, BindingResult result,
                                     Principal principal, Locale locale) throws SQLException, IllegalAccessException {
    if (result.hasErrors()) {
      ModelAndView mav = new ModelAndView("help.jsp", "message", message);
      mav.addObject("messages", new ArrayList<Message>());
      return mav;
    }
    databaseResource.addMessage(message.getMessage(), Message.SMIGO_WALL, Message.HELP,
                                 principal);
    return getHelp();
  }
}
