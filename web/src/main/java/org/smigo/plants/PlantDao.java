package org.smigo.plants;

import java.util.List;

interface PlantDao {
    List<PlantData> getPlants(int userId);

    List<PlantData> getPlants(String username);

    void addPlants(List<? extends PlantData> plants, int userId);

    void deletePlants(List<? extends PlantData> plants, int userId);

    void deletePlant(int userId, PlantData plantData);

    void addPlant(int userId, PlantData plantData);
}
