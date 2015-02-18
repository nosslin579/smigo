package org.smigo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.log.VisitLogger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Model model, HttpServletRequest request) {
        final Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        final String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        final String note = (String) request.getAttribute(VisitLogger.NOTE_ATTRIBUTE);
        log.error("Error during request. (Outside Spring MVC) Statuscode:" + statusCode, exception);
        String exMsg = exception == null ? "" : exception.getClass().getName() + ":" + exception.getMessage();
        String uriMsg = uri == null ? "" : "Uri:" + uri;
        String noteMsg = note == null ? "" : note;
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, noteMsg + "," + exMsg + "," + uriMsg);
        model.addAttribute("statusCode", statusCode);
        return "error.jsp";
    }
}
