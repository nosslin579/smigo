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

import java.util.List;

interface PlantDao {
    List<PlantDataBean> getPlants(int userId);

    List<PlantDataBean> getPlants(String username);

    void addPlants(List<PlantDataBean> plants, int userId);

    void deletePlants(List<PlantDataBean> plants, int userId);

    void deletePlant(int userId, PlantDataBean plantData);

    void addPlant(int userId, PlantDataBean plantData);
}
