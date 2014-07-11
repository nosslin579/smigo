package org.smigo.controllers;

import org.smigo.persitance.DatabaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

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

}
