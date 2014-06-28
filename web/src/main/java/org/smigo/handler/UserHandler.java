package org.smigo.handler;

import org.smigo.entities.User;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHandler {

    @Autowired
    private UserSession userSession;
    @Autowired
    private DatabaseResource databaseResource;


    public void updateUser(User user) {
        databaseResource.updateUserDetails(user);
        userSession.reloadSpecies();
        userSession.reloadGarden();
    }
}
