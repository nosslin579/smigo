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

import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.UserHandler;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PlantHandler {
    @Autowired
    private UserSession userSession;
    @Autowired
    private PlantDao plantDao;
    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private UserHandler userHandler;

    public List<PlantDataBean> getPlants(String username) {
        return plantDao.getPlants(username);
    }

    public List<PlantDataBean> getPlants(AuthenticatedUser user) {
        if (user != null) {
            return plantDao.getPlants(user.getId());
        } else {
            return userSession.getPlants();
        }
    }

    public void addPlants(List<PlantDataBean> plants, Integer userId) {
        if (!plants.isEmpty()) {
            if (userId == null) {
                userSession.getPlants().addAll(plants);
            } else {
                plantDao.addPlants(plants, userId);
            }
        }
    }

    public void addPlant(AuthenticatedUser user, PlantDataBean plantData) {
        if (user != null) {
            plantDao.addPlant(user.getId(), plantData);
        } else {
            userSession.getPlants().add(plantData);
        }
    }

    public void deletePlant(AuthenticatedUser user, PlantData plantData) {
        if (user != null) {
            plantDao.deletePlant(user.getId(), plantData);
        } else {
            userSession.getPlants().remove(plantData);
        }
    }

    public void setPlants(AuthenticatedUser user, List<PlantDataBean> plantData) {
        if (user == null) {
            userSession.getPlants().removeAll(userSession.getPlants());
            userSession.getPlants().addAll(plantData);
        }
    }

    public List<PlantDataBean> addYear(AuthenticatedUser user, int year) {
        final List<PlantDataBean> plants = getPlants(user);
        if (plants.isEmpty() || plants.stream().anyMatch(p -> p.getYear() == year)) {
            return Collections.emptyList();
        }

        int lastYear = year - 1;
        boolean containsLastYear = plants.stream().anyMatch(p -> p.getYear() == lastYear);
        int copyFromYear = containsLastYear ? lastYear : plants.stream().min((p1, p2) -> Integer.compare(p1.getYear(), p2.getYear())).get().getYear();

        List<PlantDataBean> ret = new ArrayList<>();
        for (PlantDataBean p : plants) {
            if (p.getYear() == copyFromYear && !speciesHandler.getSpecies(p.getSpeciesId()).isAnnual()) {
                p.setYear(year);
                ret.add(p);
            }
        }
        addPlants(ret, user == null ? null : user.getId());
        return ret;
    }
}