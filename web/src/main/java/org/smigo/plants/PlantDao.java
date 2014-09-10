package org.smigo.plants;

import kga.PlantData;

import java.util.List;

public interface PlantDao {
    List<PlantData> getPlants(int userId);

    void addPlants(List<? extends PlantData> plants, int userId);

    void deletePlants(List<? extends PlantData> plants, int userId);
}
