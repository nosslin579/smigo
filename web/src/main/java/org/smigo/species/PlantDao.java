package org.smigo.species;

import kga.PlantData;

import java.util.List;

public interface PlantDao {
    List<PlantData> getPlants(int userId);
}
