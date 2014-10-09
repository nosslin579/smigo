package org.smigo.plants;

import kga.Garden;
import kga.PlantData;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class PlantHandler {
    @Autowired
    private UserSession userSession;
    @Autowired
    private PlantDao plantDao;
    @Autowired
    private SpeciesHandler speciesHandler;

    public void updateGarden(AuthenticatedUser user, UpdateGardenBean updateGardenBean) {
        if (updateGardenBean.getAddList().isEmpty() && updateGardenBean.getRemoveList().isEmpty()) {
            return;
        }
        if (user != null) {
            plantDao.addPlants(updateGardenBean.getAddList(), user.getId());
            plantDao.deletePlants(updateGardenBean.getRemoveList(), user.getId());
        } else {
            userSession.getPlants().removeAll(updateGardenBean.getRemoveList());
            userSession.getPlants().addAll(updateGardenBean.getAddList());
        }
    }

    public List<PlantData> getPlants(AuthenticatedUser user) {
        if (user != null) {
            return plantDao.getPlants(user.getId());
        } else {
            return userSession.getPlants();
        }
    }

    public Garden getGarden(AuthenticatedUser user, Locale locale) {
        return new Garden(speciesHandler.getSpeciesMap(user, locale), getPlants(user));
    }

    public void addPlants(List<PlantData> plants, int userId) {
        if (!plants.isEmpty()) {
            plantDao.addPlants(plants, userId);
        }
    }
}
