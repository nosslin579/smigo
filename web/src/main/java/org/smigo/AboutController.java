package org.smigo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.config.VisitLogger;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.UserAdaptiveMessageSource;
import org.smigo.user.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Locale;

@Controller
public class AboutController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserHandler userHandler;
    @Autowired
    private UserAdaptiveMessageSource messageSource;
    @Autowired
    private SpeciesHandler speciesHandler;

    @RequestMapping(value = {"/garden", "/"}, method = RequestMethod.GET)
    public String getGarden(Model model, Locale locale, Principal principal) {
        model.addAttribute("user", userHandler.getUser(principal));
        model.addAttribute("garden", speciesHandler.getGarden());
        model.addAttribute("messages", messageSource.getAllMessages(locale));
        return "ng.jsp";
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
