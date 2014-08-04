package org.smigo.species;

import kga.PlantData;
import org.smigo.entities.PlantDataBean;

import java.util.List;

public interface PlantDao {
    List<PlantData> getPlants(int userId);

    void addPlants(List<PlantDataBean> plants, int userId);

    void deletePlants(List<PlantDataBean> plants, int userId);
}
