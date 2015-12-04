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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Locale;

@Controller
public class AcceptTermsOfServiceController {

    @Autowired
    private UserHandler userHandler;

    @RequestMapping(value = "/accept-terms-of-service", method = RequestMethod.GET)
    public String acceptTermsOfService(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
        if (user != null && !userHandler.getUser(user).isTermsOfService()) {
            model.addAttribute(new AcceptTermsOfService());
            return "accept-tos.jsp";
        }
        return "redirect:/garden-planner";
    }

    @RequestMapping(value = "/accept-terms-of-service", method = RequestMethod.POST)
    public String acceptTermsOfService(@Valid AcceptTermsOfService acceptTermsOfService, BindingResult result,
                                       Locale locale, @AuthenticationPrincipal AuthenticatedUser user) {
        if (result.hasErrors()) {
            return "accept-tos.jsp";
        }
        userHandler.acceptTermsOfService(user);
        return "redirect:/garden-planner";
    }
}
