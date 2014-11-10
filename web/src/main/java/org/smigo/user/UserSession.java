package org.smigo.user;

import org.smigo.plants.PlantData;

import java.io.Serializable;
import java.util.List;


public interface UserSession extends Serializable {

    List<PlantData> getPlants();

    void setUser(UserBean user);

    UserBean getUser();
}
