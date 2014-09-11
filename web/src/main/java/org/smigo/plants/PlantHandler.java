package org.smigo.plants;

import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlantHandler {
    @Autowired
    private UserSession userSession;
    @Autowired
    private PlantDao plantDao;

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
}
