package org.smigo.user;

import kga.PlantData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface UserSession extends Serializable {

    Map<String, String> getTranslation();

    List<PlantData> getPlants();

    void setUser(UserBean user);

    UserBean getUser();
}
