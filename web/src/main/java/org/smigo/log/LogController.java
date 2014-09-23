package org.smigo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Controller
public class LogController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/rest/log", method = RequestMethod.POST)
    @ResponseBody
    public void log(@RequestBody ReferenceError referenceError, HttpServletRequest request) {
        log.error("Angular error " + referenceError);
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, referenceError.getStack());
    }
}