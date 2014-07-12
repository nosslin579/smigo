package org.smigo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@Controller
public class AboutController {

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
