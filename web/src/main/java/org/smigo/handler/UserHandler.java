package org.smigo.handler;

import kga.PlantData;
import org.smigo.entities.User;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserHandler {

    @Autowired
    private UserSession userSession;
    @Autowired
    private DatabaseResource databaseResource;


    public void updateUser(User user) {
        databaseResource.updateUserDetails(user);
    }

    public void createUser(User user, long decideTime) {
        long signupTime = userSession.getSignupTime();
        databaseResource.addUser(user, signupTime, decideTime);

//        List<PlantDb> plants = PlantConverter.convert(userSession.getGarden().getAllSquares());
        List<PlantData> plants = userSession.getPlants();
        if (!plants.isEmpty()) {
            final User user1 = databaseResource.getUser(user.getUsername());
            databaseResource.updateGarden(user1.getId(), plants);
        }

    }
}
