package org.smigo.persitance;

import kga.Garden;
import kga.PlantData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface UserSession extends Serializable {

    void registerSignupStart();

    long getSignupTime();

    Map<String, String> getTranslation();

    List<PlantData> getPlants();
}
