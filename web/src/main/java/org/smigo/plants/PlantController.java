package org.smigo.plants;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Controller
public class PlantController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlantHandler plantHandler;

    @InitBinder
    public void initListBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(10000);
    }

    @RequestMapping(value = "/rest/plant", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<PlantData> getPlants(@AuthenticationPrincipal AuthenticatedUser user) {
        return plantHandler.getPlants(user);
    }

    @RequestMapping(value = "/rest/plant/{username}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<PlantData> getPlants(@PathVariable String username) {
        return plantHandler.getPlants(username);
    }

    @RequestMapping(value = {"/rest/plant"}, method = RequestMethod.POST)
    @ResponseBody
    public void updateGarden(@RequestBody PlantDataBean plantData, @AuthenticationPrincipal AuthenticatedUser user) {
        plantHandler.addPlant(user, plantData);
    }

    @RequestMapping(value = {"/rest/plant/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public void deletePlant(@RequestBody PlantDataBean plantData, @AuthenticationPrincipal AuthenticatedUser user) {
        plantHandler.deletePlant(user, plantData);
    }

    @RequestMapping(value = {"/plant/upload"}, method = RequestMethod.POST)
    public String upload(@Valid UploadBean uploadBean, @AuthenticationPrincipal AuthenticatedUser user) {
        plantHandler.setPlants(user, uploadBean.getPlants());
        return "redirect:/";
    }
}
