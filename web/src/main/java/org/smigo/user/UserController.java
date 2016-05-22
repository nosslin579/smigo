package org.smigo.user;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserHandler userHandler;
    @Autowired
    private UserAdaptiveMessageSource messageSource;
    @Autowired
    private SpeciesHandler speciesHandler;

    public UserController() {
        log.debug("Creating new UserController");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Locale.class, new LocaleEditor());
    }


    @RequestMapping(value = "/rest/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@AuthenticationPrincipal AuthenticatedUser user) {
        return userHandler.getUser(user);
    }


    @RequestMapping(value = "/rest/user/{username}", method = RequestMethod.GET)
    @ResponseBody
    public UserPublic getUser(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable String username) {
        return userHandler.getUserPublicInfo(username);
    }

    @RequestMapping(value = "/rest/user", method = RequestMethod.POST)
    @ResponseBody
    public List<ObjectError> addUser(@RequestBody @Valid UserAdd user, BindingResult result, HttpServletResponse response, Locale locale) {
        log.info("Create user: " + user);
        if (result.hasErrors()) {
            log.warn("Create user failed. Username:" + user.getUsername() + " Errors:" + StringUtils.arrayToDelimitedString(result.getAllErrors().toArray(), ", "));
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return result.getAllErrors();
        }
        userHandler.createUser(user, locale);
        return Collections.emptyList();
    }

    @RequestMapping(value = "/rest/user", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ObjectError> updateUser(@RequestBody @Valid User userBean, BindingResult result,
                                        @AuthenticationPrincipal AuthenticatedUser user,
                                        HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("Update user failed. Username:" + user.getUsername() + " Errors:" + StringUtils.arrayToDelimitedString(result.getAllErrors().toArray(), ", "));
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return result.getAllErrors();
        }
        log.info("Updating user: " + userBean.toString());
        userHandler.updateUser(user.getId(), userBean);
        return Collections.emptyList();
    }

    @RequestMapping(value = "/locales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public java.util.Map<String, String> getLocales(HttpServletRequest request) {
        return Language.getLanguagesForDisplay(request.getLocales());
    }

    @RequestMapping(value = {"/rest/translation"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public java.util.Map<Object, Object> getDefaultTranslation(Locale locale) {
        return messageSource.getAllMessages(locale);
    }
}
