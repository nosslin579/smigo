package org.smigo.species.varieties;

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
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;

@RestController
public class VarietyController implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(VarietyController.class);

    @Autowired
    private VarietyDao varietyDao;

    @RequestMapping(value = "/rest/variety", method = RequestMethod.GET)
    public Collection<Variety> getVarieties(@RequestParam(required = false) Integer userId) {
        return userId == null ? varietyDao.getVarieties() : varietyDao.getVarietiesByUser(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rest/variety", method = RequestMethod.POST)
    public Object addVariety(@Valid @RequestBody Variety variety, BindingResult result, HttpServletResponse response,
                             @AuthenticationPrincipal AuthenticatedUser user) {
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return result.getAllErrors();
        }
        variety.setUserId(user.getId());
        final int id = varietyDao.addVariety(variety);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return id;
    }
}
