package org.smigo.plants;

import kga.PlantData;

import java.util.List;

interface PlantDao {
    List<PlantData> getPlants(int userId);

    List<PlantData> getPlants(String username);

    void addPlants(List<? extends PlantData> plants, int userId);

    void deletePlants(List<? extends PlantData> plants, int userId);

}
