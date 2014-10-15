package org.smigo.plants;

import kga.PlantData;
import kga.Square;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/")
public class PlantController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlantHandler plantHandler;

    @RequestMapping(value = "/rest/plant", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<PlantData> getPlants(@AuthenticationPrincipal AuthenticatedUser user) {
        return plantHandler.getPlants(user);
    }

    @RequestMapping(value = "/rest/plant/{userId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Map<Integer, Collection<Square>> getPlants(@PathVariable Integer userId, Locale locale) {
        return plantHandler.getGarden(new Guest(userId), locale).getSquares();
    }

    @RequestMapping(value = {"/update-garden", "/rest/plant"}, produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public void updateGarden(@RequestBody UpdateGardenBean updateGardenBean, @AuthenticationPrincipal AuthenticatedUser user) {
        plantHandler.updateGarden(user, updateGardenBean);
    }
}