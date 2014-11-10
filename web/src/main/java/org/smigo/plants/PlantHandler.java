package org.smigo.plants;

import org.smigo.species.SpeciesHandler;
import org.smigo.user.User;
import org.smigo.user.UserHandler;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlantHandler {
    @Autowired
    private UserSession userSession;
    @Autowired
    private PlantDao plantDao;
    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private UserHandler userHandler;

    public void updateGarden(User user, UpdateGardenBean updateGardenBean) {
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

    public List<PlantData> getPlants(String username) {
        return plantDao.getPlants(username);
    }

    public List<PlantData> getPlants(User user) {
        if (user != null) {
            return plantDao.getPlants(user.getId());
        } else {
            return userSession.getPlants();
        }
    }

    public void addPlants(List<PlantData> plants, int userId) {
        if (!plants.isEmpty()) {
            plantDao.addPlants(plants, userId);
        }
    }
}
