package org.smigo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Controller
public class LogController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserSession userSession;

    @RequestMapping(value = "/rest/log/error", method = RequestMethod.POST)
    @ResponseBody
    public void logError(@RequestBody ReferenceError referenceError, HttpServletRequest request) {
        log.error("Angular error:\n" + referenceError + "\nPlants:\n" + StringUtils.arrayToDelimitedString(userSession.getPlants().toArray(), ","));
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, referenceError.getStack());
    }

    @RequestMapping(value = "/rest/log/feature", method = RequestMethod.POST)
    @ResponseBody
    public void logFeatureRequest(@RequestBody FeatureRequest feature, HttpServletRequest request) {
        request.setAttribute(VisitLogger.NOTE_ATTRIBUTE, feature.getFeature());
    }
}