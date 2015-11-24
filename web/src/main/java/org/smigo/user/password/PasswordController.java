package org.smigo.user.password;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserAdaptiveMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Controller that handles user specific type of requests.
 *
 * @author Christian Nilsson
 */
@Controller
public class PasswordController {

    private final Logger log = LoggerFactory.getLogger(PasswordController.class);
    @Autowired
    private PasswordHandler userHandler;
    @Autowired
    private UserAdaptiveMessageSource messageSource;
    @Autowired
    private SpeciesHandler speciesHandler;

    public PasswordController() {
        log.debug("Creating new UserController");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Locale.class, new LocaleEditor());
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @ResponseBody
    public List<ObjectError> changePassword(@RequestBody @Valid PasswordFormBean passwordFormBean, BindingResult result,
                                            @AuthenticationPrincipal AuthenticatedUser user, HttpServletResponse response) {
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return result.getAllErrors();
        }
        userHandler.updatePassword(user.getId(), passwordFormBean.getNewPassword());
        return Collections.emptyList();
    }

    @RequestMapping(value = "/request-password-link", method = RequestMethod.POST)
    @ResponseBody
    public void requestPasswordLink(@RequestBody RequestPasswordLinkFormBean bean) {
        userHandler.sendResetPasswordEmail(bean.getEmail());
    }

    @RequestMapping(value = "/reset-password/{resetKey}", method = RequestMethod.GET)
    public String getResetForm(@PathVariable String resetKey, Model model, HttpServletResponse response) {
        model.addAttribute(new ResetKeyPasswordFormBean(resetKey));
        return "resetpasswordform.jsp";
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public String setPassword(@Valid ResetKeyPasswordFormBean resetFormBean, BindingResult result) {
        if (result.hasErrors()) {
            return "resetpasswordform.jsp";
        }
        userHandler.setPassword(resetFormBean);
        return "redirect:/login";
    }

}
