package org.smigo.plants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@Controller
public class PlantController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlantHandler plantHandler;

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
}