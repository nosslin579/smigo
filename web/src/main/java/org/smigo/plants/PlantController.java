package org.smigo.plants;

import kga.Square;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/")
public class PlantController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private PlantHandler plantHandler;

    @RequestMapping(value = "/rest/plant", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Map<Integer, Collection<Square>> getPlants() {
        return speciesHandler.getGarden().getSquares();
    }

    @RequestMapping(value = {"/update-garden", "/rest/plant"}, produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public void updateGarden(@RequestBody UpdateGardenBean updateGardenBean, @AuthenticationPrincipal AuthenticatedUser user) {
        plantHandler.updateGarden(user, updateGardenBean);
    }
}