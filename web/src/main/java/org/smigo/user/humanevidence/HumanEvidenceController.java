package org.smigo.user.humanevidence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class HumanEvidenceController {

    private final Logger log = LoggerFactory.getLogger(HumanEvidenceController.class);

    @Autowired
    private HumanEvidenceHandler humanEvidenceHandler;

    @RequestMapping(value = "/rpc/showcaptcha", method = RequestMethod.GET)
    @ResponseBody
    public boolean showCaptcha (HttpServletRequest request) {
        return !humanEvidenceHandler.isVerifiedHuman(request);
    }
}
