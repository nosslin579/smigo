package org.smigo.user;

import kga.PlantData;

import java.io.Serializable;
import java.util.List;


public interface UserSession extends Serializable {

    List<PlantData> getPlants();

    void setUser(UserBean user);

    UserBean getUser();
}
