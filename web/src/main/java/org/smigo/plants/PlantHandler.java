package org.smigo.plants;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.smigo.message.MessageHandler;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PlantHandler {
    @Autowired
    private UserSession userSession;
    @Autowired
    private PlantDao plantDao;
    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private MessageHandler messageHandler;

    private AtomicInteger plantId = new AtomicInteger(0);

    public List<Plant> getPlants(String username) {
        return plantDao.getPlants(username);
    }

    public List<Plant> getPlants(AuthenticatedUser user) {
        if (user != null) {
            return plantDao.getPlants(user.getId());
        } else {
            return userSession.getPlants();
        }
    }

    public List<Plant> getPlants(int speciesId) {
        return plantDao.getPlantsBySpecies(speciesId);
    }

    public List<Plant> addPlants(List<Plant> plants, Integer userId) {
        if (plants.isEmpty()) {
            return plants;
        }
        if (userId == null) {
            userSession.getPlants().addAll(plants);
            plants.forEach(plant -> plant.setId(plantId.incrementAndGet()));
            return plants;
        }

        plants.forEach(plant -> {
            plant.setUserId(userId);
            int id = plantDao.addPlant(plant);
            plant.setId(id);
        });
        return plants;
    }

    public int addPlant(AuthenticatedUser user, Plant plant) {
        if (user != null) {
            plant.setUserId(user.getId());
            return plantDao.addPlant(plant);
        } else {
            plant.setId(plantId.incrementAndGet());
            userSession.getPlants().add(plant);
            return plant.getId();
        }
    }

    public void deletePlant(AuthenticatedUser user, int plantId) {
        if (user != null) {
            int numOfPlantsDeleted = plantDao.deletePlant(user.getId(), plantId);
            if (numOfPlantsDeleted != 1) {
                throw new java.lang.IllegalArgumentException("Delete plant returned other then one row affected:" + numOfPlantsDeleted);
            }
        } else {
            userSession.getPlants().removeIf(p -> p.getId() == plantId);
        }
    }

    public void setPlants(AuthenticatedUser user, List<Plant> plant) {
        if (user == null) {
            userSession.getPlants().removeIf(p -> true);
            userSession.getPlants().addAll(plant);
            userSession.getPlants().forEach(p -> p.setId(plantId.incrementAndGet()));
        }
    }

    public List<Plant> addYear(AuthenticatedUser user, int theNewYear, Locale locale) {
        final List<Plant> currentPlants = getPlants(user);
        if (currentPlants.isEmpty() || currentPlants.stream().anyMatch(p -> p.getYear() == theNewYear)) {
            return Collections.emptyList();
        }

        int lastYear = theNewYear - 1;
        boolean containsLastYear = currentPlants.stream().anyMatch(p -> p.getYear() == lastYear);
        int copyFromYear = containsLastYear ? lastYear : currentPlants.stream().min((p1, p2) -> Integer.compare(p1.getYear(), p2.getYear())).get().getYear();

        List<Plant> newYearPlants = new ArrayList<>();
        for (Plant p : currentPlants) {
            if (p.getYear() == copyFromYear && !speciesHandler.getSpecies(p.getSpeciesId()).isAnnual()) {
                Plant newPlant = new Plant(p);
                newPlant.setYear(theNewYear);
                newYearPlants.add(newPlant);
            }
        }
        messageHandler.addNewYearNewsMessage(Optional.ofNullable(user), theNewYear, currentPlants, locale);
        return addPlants(newYearPlants, user == null ? null : user.getId());
    }

    public void replaceSpecies(int oldSpeciesId, int newSpeciesId) {
        plantDao.replaceSpecies(oldSpeciesId, newSpeciesId);
    }
}