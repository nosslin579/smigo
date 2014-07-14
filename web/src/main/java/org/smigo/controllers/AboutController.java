package org.smigo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.config.VisitLogger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Controller
public class AboutController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
    public String error(HttpServletRequest request) {
        final Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        log.error("Error during request. (Outside Spring MVC)", exception);
        final String note = "Exception:" + exception;
        final String noteLengthChecked = note.length() > 255 ? note.substring(0, 255) : note;
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, noteLengthChecked);
        return "error.jsp";
    }

}
