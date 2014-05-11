package org.smigo.handler;

import org.smigo.entities.User;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserHandler {

  @Autowired
  private UserSession userSession;
  @Autowired
  private DatabaseResource databaseResource;

  public void reloadUser(String name) {
    User user = databaseResource.getUser(name);
    userSession.setUser(user);
    userSession.reloadSpecies();
    userSession.reloadGarden();
  }


  public void updateUser(User user) {
    user.setId(userSession.getUser().getId());
    databaseResource.updateUserDetails(user);
    reloadUser(userSession.getUser().getUsername());
  }
}
